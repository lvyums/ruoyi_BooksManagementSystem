package com.ruoyi.system.library.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.library.domain.Reader;
import com.ruoyi.system.library.mapper.BorrowMapper;
import com.ruoyi.system.library.mapper.ReaderMapper;
import com.ruoyi.system.library.service.IReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * 读者信息Service业务层处理
 * 
 * @author lvyu
 * @date 2025-12-14
 */
@Service
public class ReaderServiceImpl implements IReaderService 
{
    @Autowired
    private ReaderMapper readerMapper;

    @Autowired // 1. 注入若依用户服务
    private ISysUserService sysUserService;

    @Autowired
    private BorrowMapper borrowMapper; // 注入借阅Mapper


    /**
     * 补办借书证
     * @param oldRdID 原借书证号
     * @return 新借书证号
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 开启事务
    public Long reissueCard(Long oldRdID) {
        // 1. 查询原读者
        Reader oldReader = readerMapper.selectReaderByRdID(oldRdID);
        if (oldReader == null) {
            throw new ServiceException("原借书证号不存在");
        }
        // 如果已经注销，不允许补办
        if ("2".equals(oldReader.getRdStatus())) {
            throw new ServiceException("该借书证已注销，无法补办");
        }
        // 2. 创建新读者 (复制原读者信息)
        Reader newReader = new Reader();
        // 复制属性 (Spring的工具类)
        org.springframework.beans.BeanUtils.copyProperties(oldReader, newReader);
        // 3. 设置新卡特有属性
        newReader.setRdID(null); // ID置空，让数据库自动生成新ID
        newReader.setRdStatus("0"); // 新卡状态：有效
        newReader.setRdDateReg(new Date()); // 办证日期：今天
        newReader.setRdBorrowQty(oldReader.getRdBorrowQty()); // 借书数量也要同步过来
        // 4. 插入新读者 -> 产生新ID
        readerMapper.insertReader(newReader);
        Long newRdID = newReader.getRdID();
        // 5. 【重要】同步新账号到 sys_user (调用你之前写的同步方法)
        syncToSysUser(newReader);
        // 6. 【核心】转移借阅记录：把旧ID的借书记录全部转到新ID名下
        borrowMapper.transferBorrowRecords(oldRdID, newRdID);
        // 7. 注销旧借书证
        // 因为借阅记录已经转走了，现在旧号名下没有书了，可以放心地注销了
        oldReader.setRdStatus("2"); // 2 代表注销/挂失
        oldReader.setRdBorrowQty(Long.valueOf(0)); // 旧卡借书量清零
        readerMapper.updateReader(oldReader);
        // 8. 禁用旧账号的登录权限 (sys_user)
        SysUser oldSysUser = sysUserService.selectUserByUserName(String.valueOf(oldRdID));
        if (oldSysUser != null) {
            oldSysUser.setStatus("1"); // 1 代表停用
            sysUserService.updateUserStatus(oldSysUser);
        }
        return newRdID;
    }


    /**
     * 查询读者信息
     * 
     * @param rdID 读者信息主键
     * @return 读者信息
     */
    @Override
    public Reader selectReaderByRdID(Long rdID)
    {
        return readerMapper.selectReaderByRdID(rdID);
    }

    /**
     * 查询读者信息列表
     * 
     * @param reader 读者信息
     * @return 读者信息
     */
    @Override
    public List<Reader> selectReaderList(Reader reader)
    {
        return readerMapper.selectReaderList(reader);
    }

    /**
     * 新增读者信息
     *
     * @param reader 读者信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertReader(Reader reader)
    {
        reader.setRdPwd("123456"); // 默认密码
        reader.setRdBorrowQty(Long.valueOf(0)); // 初始已借数量

        // 如果前端没传状态，默认为有效(0)
        if (reader.getRdStatus() == null) {
            reader.setRdStatus("0");
        }

        // 如果前端没传办证日期，默认为当前时间
        if (reader.getRdDateReg() == null) {
            reader.setRdDateReg(DateUtils.getNowDate());
        }

        // === 2. 插入读者表 ===
        int rows = readerMapper.insertReader(reader);

        // === 3. 新增功能：同步到系统用户表 ===
        syncToSysUser(reader);

        return rows;
    }

    /**
     * 修改读者信息
     *
     * @param reader 读者信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateReader(Reader reader)
    {
        // 检查业务规则：如果是执行注销操作 (rdStatus = 2)
        if ("2".equals(reader.getRdStatus())) {
            // 查询数据库中该读者的当前信息
            Reader dbReader = readerMapper.selectReaderByRdID(reader.getRdID());

            // 校验：如果有未归还图书，禁止注销
            if (dbReader != null && dbReader.getRdBorrowQty() != null && dbReader.getRdBorrowQty() > 0) {
                throw new ServiceException("该读者仍有 " + dbReader.getRdBorrowQty() + " 本书未归还，无法注销借书证！");
            }
        }
        // === 2. 更新读者表 ===
        int rows = readerMapper.updateReader(reader);

        // === 3. 新增功能：同步更新系统用户表 ===
        // 为了保证数据一致性，每次修改读者都尝试同步一下账号
        syncToSysUser(reader);

        return rows;
    }

    /**
     * 批量删除读者信息
     * 
     * @param rdIDs 需要删除的读者信息主键
     * @return 结果
     */
    @Override
    public int deleteReaderByRdIDs(Long[] rdIDs)
    {
        for (Long rdID : rdIDs) {
            Reader reader = readerMapper.selectReaderByRdID(rdID);
            // 校验：有未还图书不能删除
            if (reader != null && reader.getRdBorrowQty() != null && reader.getRdBorrowQty() > 0) {
                throw new ServiceException("读者[" + reader.getRdName() + "]仍有图书未还，无法删除！");
            }
        }
        return readerMapper.deleteReaderByRdIDs(rdIDs);
    }

    /**
     * 删除读者信息信息
     * 
     * @param rdID 读者信息主键
     * @return 结果
     */
    @Override
    public int deleteReaderByRdID(Long rdID)
    {
        Reader reader = readerMapper.selectReaderByRdID(rdID);
        if (reader != null && reader.getRdBorrowQty() != null && reader.getRdBorrowQty() > 0) {
            throw new ServiceException("该读者仍有图书未还，无法删除！");
        }
        return readerMapper.deleteReaderByRdID(rdID);
    }
    // ==========================================================
    //                  核心辅助方法：同步账号
    // ==========================================================

    /**
     * 将读者信息同步到若依系统用户表 (sys_user)
     */
    private void syncToSysUser(Reader reader) {
        try {
            SysUser user = new SysUser();
            user.setUserName(String.valueOf(reader.getRdID()));
            user.setNickName(reader.getRdName());
            user.setPassword(reader.getRdPwd());
            user.setDeptId(100L); // 默认部门ID
            user.setStatus("0");  // 正常状态
            user.setPhonenumber(reader.getRdPhone());
            user.setEmail(reader.getRdEmail());
            user.setSex(reader.getRdSex());

            // === 角色映射逻辑 ===
            Long ROLE_CARD_ADMIN = 100L;   // 借书证管理员
            Long ROLE_BOOK_ADMIN = 101L;   // 图书管理员
            Long ROLE_BORROW_ADMIN = 102L; // 借阅管理员
            Long ROLE_SYS_ADMIN = 103L;    // 系统管理员
            Long ROLE_COMMON_READER = 2L; // 普通读者

            List<Long> roleIds = new ArrayList<>();
            int bitRole = (reader.getRdAdminRoles() == null) ? 0 : reader.getRdAdminRoles().intValue();
            // 如果是管理员，添加对应角色
            if ((bitRole & 1) == 1) roleIds.add(ROLE_CARD_ADMIN);
            if ((bitRole & 2) == 2) roleIds.add(ROLE_BOOK_ADMIN);
            if ((bitRole & 4) == 4) roleIds.add(ROLE_BORROW_ADMIN);
            if ((bitRole & 8) == 8) roleIds.add(ROLE_SYS_ADMIN);
            // 如果没有任何管理权限，就是普通读者
            if (roleIds.isEmpty()) {
                roleIds.add(ROLE_COMMON_READER);
            }
            user.setRoleIds(roleIds.toArray(new Long[0]));
            // === 数据库操作 ===
            // 检查账号是否存在
            SysUser oldUser = sysUserService.selectUserByUserName(String.valueOf(user.getUserName()));
            if (oldUser == null) {
                sysUserService.insertUser(user);
            } else {
                // 已存在则更新
                user.setUserId(oldUser.getUserId());
                sysUserService.updateUser(user);
                // 强制重置密码和角色
                sysUserService.resetUserPwd(user.getUserId(), user.getPassword());
                sysUserService.insertUserAuth(user.getUserId(), user.getRoleIds());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("同步系统账号失败: " + e.getMessage());
        }
    }
}
