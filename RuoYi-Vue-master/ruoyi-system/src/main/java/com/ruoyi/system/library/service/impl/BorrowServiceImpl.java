package com.ruoyi.system.library.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.library.domain.Book;
import com.ruoyi.system.library.domain.Borrow;
import com.ruoyi.system.library.mapper.BookMapper;
import com.ruoyi.system.library.mapper.BorrowMapper;
import com.ruoyi.system.library.mapper.ReaderMapper;
import com.ruoyi.system.library.mapper.ReadertypeMapper;
import com.ruoyi.system.library.service.IBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 借阅记录Service业务层处理
 * 
 * @author lvyu
 * @date 2025-12-14
 */
@Service
public class BorrowServiceImpl implements IBorrowService 
{
    @Autowired
    private BorrowMapper borrowMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ReaderMapper readerMapper;

    @Autowired
    private ReadertypeMapper readertypeMapper;

    /**
     * 查询借阅记录
     * 
     * @param BorrowID 借阅记录主键
     * @return 借阅记录
     */
    @Override
    public Borrow selectBorrowByBorrowID(Long BorrowID)
    {
        return borrowMapper.selectBorrowByBorrowID(BorrowID);
    }


    /**
     * 查询借阅记录列表
     * 
     * @param borrow 借阅记录
     * @return 借阅记录
     */
    @Override
    public List<Borrow> selectBorrowList(Borrow borrow)
    {
        try {
            // 1. 获取当前登录用户
            if (SecurityUtils.getLoginUser() == null) {
                return borrowMapper.selectBorrowList(borrow);
            }
            SysUser currentUser = SecurityUtils.getLoginUser().getUser();
            boolean isBigBoss = currentUser.isAdmin();
            // 检查是否有借阅管理员角色
            boolean isManager = currentUser.getRoles().stream()
                    .anyMatch(r -> "borrow_admin".equals(r.getRoleKey()));
            // 3. 如果既不是超管，也不是借阅管理员 -> 只查自己
            if (!isBigBoss && !isManager) {
                borrow.setRdID(Long.valueOf(currentUser.getUserName()));
            }
        } catch (Exception e) {
        }
        return borrowMapper.selectBorrowList(borrow);
    }


    /**
     * 新增借阅记录
     * 
     * @param borrow 借阅记录
     * @return 结果
     */
    @Override
    public int insertBorrow(Borrow borrow)
    {
        return borrowMapper.insertBorrow(borrow);
    }

    /**
     * 修改借阅记录
     * 
     * @param borrow 借阅记录
     * @return 结果
     */
    @Override
    public int updateBorrow(Borrow borrow)
    {
        return borrowMapper.updateBorrow(borrow);
    }

    /**
     * 批量删除借阅记录
     * 
     * @param BorrowIDs 需要删除的借阅记录主键
     * @return 结果
     */
    @Override
    public int deleteBorrowByBorrowIDs(Long[] BorrowIDs)
    {
        return borrowMapper.deleteBorrowByBorrowIDs(BorrowIDs);
    }

    /**
     * 删除借阅记录信息
     * 
     * @param BorrowID 借阅记录主键
     * @return 结果
     */
    @Override
    public int deleteBorrowByBorrowID(Long BorrowID)
    {
        return borrowMapper.deleteBorrowByBorrowID(BorrowID);
    }
    // =========================================================================
    //                            以下为核心业务逻辑实现
    // =========================================================================

    /**
     * 办理借书
     * 业务流程：
     * 1. 检查读者资格（状态、限额）
     * 2. 检查图书状态（是否在馆）
     * 3. 创建借阅记录（计算应还日期）
     * 4. 更新图书状态 -> 借出
     * 5. 更新读者借书量 -> +1
     */
    @Override
    @Transactional
    public void borrowBook(Long rdID, Long bkID) {

        // 【新增逻辑】检查是否有超期未还
        int overdueCount = borrowMapper.countOverdueByRdID(rdID);
        if (overdueCount > 0) {
            throw new ServiceException("该读者有 " + overdueCount + " 本超期图书未归还，系统锁定！请先归还超期书籍。");
        }
        // 1. 获取并校验读者
        com.ruoyi.system.library.domain.Reader reader = readerMapper.selectReaderByRdID(rdID);
        if (reader == null) throw new ServiceException("读者ID不存在");
        if (!"0".equals(reader.getRdStatus())) {
            throw new ServiceException("该借书证处于挂失或注销状态，无法借阅");
        }

        // 2. 获取读者类别规则（限额、限期）
        com.ruoyi.system.library.domain.Readertype type = readertypeMapper.selectReadertypeByRdType(reader.getRdType());
        if (type == null) throw new ServiceException("读者类别异常");

        // 规则：如果 DateValid > 0 (非永久)，需计算到期时间
        if (type.getDateValid() != null && type.getDateValid() > 0) {
            Date regDate = reader.getRdDateReg(); // 办证日期
            if (regDate != null) {
                // 计算过期时间：办证日期 + 有效年限
                Date expireDate = DateUtils.addMonths(regDate, (int) (type.getDateValid() * 12));

                // 如果当前时间在过期时间之后
                if (DateUtils.getNowDate().after(expireDate)) {
                    throw new ServiceException("您的借书证已过期（有效期至：" + DateUtils.parseDateToStr("yyyy-MM-dd", expireDate) + "），请先补办或更新信息。");
                }
            }
        }

        if (reader.getRdBorrowQty() >= type.getCanLendQty()) {
            throw new ServiceException("已达到最大借书数量限制 (" + type.getCanLendQty() + "本)");
        }

        // 3. 获取并校验图书
        Book book = bookMapper.selectBookByBkID(bkID);
        if (book == null) throw new ServiceException("图书ID不存在");
        if (!"0".equals(book.getBkStatus())) {
            throw new ServiceException("图书当前不是在馆状态，无法借阅");
        }

        // 4. 创建借阅记录
        Borrow borrow = new Borrow();
        borrow.setRdID(rdID);
        borrow.setBkID(bkID);
        borrow.setLdDateOut(DateUtils.getNowDate()); // 借出时间
        borrow.setLdContinueTimes(0L); // 续借次数初始化
        borrow.setLsHasReturn("0");   // 未还

        // 计算应还日期 = 当前日期 + 可借天数
        Date planDate = DateUtils.addDays(DateUtils.getNowDate(), type.getCanLendDay().intValue());
        borrow.setLdDateRetPlan(planDate);

        try {
            borrow.setOperatorBorrow(SecurityUtils.getUsername()); // 操作员
        } catch (Exception e) {
            borrow.setOperatorBorrow("System"); // 兜底
        }

        borrowMapper.insertBorrow(borrow);

        // 5. 更新图书状态为 "1" (借出)
        book.setBkStatus("1");
        bookMapper.updateBook(book);

        // 6. 更新读者已借数量 +1
        reader.setRdBorrowQty(reader.getRdBorrowQty() + 1);
        readerMapper.updateReader(reader);
    }

    /**
     * 办理续借
     * 业务流程：
     * 1. 检查借阅记录状态
     * 2. 检查续借次数限制
     * 3. 更新借阅记录（次数+1，应还日期延后）
     */
    @Override
    @Transactional
    public void renewBook(Long borrowID) {
        Borrow borrow = borrowMapper.selectBorrowByBorrowID(borrowID);
        // 检查该读者的超期情况
        int overdueCount = borrowMapper.countOverdueByRdID(borrow.getRdID());
        if (overdueCount > 0) {
            throw new ServiceException("您有超期书籍未还，无法办理续借业务！");
        }
        if (borrow == null) throw new ServiceException("借阅记录不存在");
        if ("1".equals(borrow.getLsHasReturn())) {
            throw new ServiceException("该图书已归还，无法续借");
        }
        // 获取规则
        com.ruoyi.system.library.domain.Reader reader = readerMapper.selectReaderByRdID(borrow.getRdID());
        com.ruoyi.system.library.domain.Readertype type = readertypeMapper.selectReadertypeByRdType(reader.getRdType());
        // 校验次数
        if (borrow.getLdContinueTimes() >= type.getCanContinueTimes()) {
            throw new ServiceException("已达到最大续借次数限制 (" + type.getCanContinueTimes() + "次)");
        }
        // 校验读者状态
        if (!"0".equals(reader.getRdStatus())) {
            throw new ServiceException("读者状态异常，无法续借");
        }
        // 执行续借
        borrow.setLdContinueTimes(borrow.getLdContinueTimes() + 1);
        // 新的应还日期 = 原应还日期 + 可借天数
        Date newPlanDate = DateUtils.addDays(borrow.getLdDateRetPlan(), type.getCanLendDay().intValue());
        borrow.setLdDateRetPlan(newPlanDate);
        borrowMapper.updateBorrow(borrow);
    }

    /**
     * 办理还书
     * 业务流程：
     * 1. 计算超期天数
     * 2. 计算罚款金额（超期罚款 or 遗失赔偿）
     * 3. 更新借阅记录（设为已还、填入罚金）
     * 4. 更新图书状态（在馆 or 遗失）
     * 5. 更新读者借书量 -> -1
     */
    @Override
    @Transactional
    public void returnBook(Long borrowID, Boolean isLost) {
        Borrow borrow = borrowMapper.selectBorrowByBorrowID(borrowID);
        if (borrow == null) throw new ServiceException("借阅记录不存在");
        if ("1".equals(borrow.getLsHasReturn())) {
            throw new ServiceException("该图书已经归还过了");
        }
        com.ruoyi.system.library.domain.Reader reader = readerMapper.selectReaderByRdID(borrow.getRdID());
        com.ruoyi.system.library.domain.Readertype type = readertypeMapper.selectReadertypeByRdType(reader.getRdType());
        Book book = bookMapper.selectBookByBkID(borrow.getBkID());
        Date nowDate = DateUtils.getNowDate();
        int overDays = 0;
        BigDecimal fine = BigDecimal.ZERO;
        // 1. 计算超期
        if (nowDate.after(borrow.getLdDateRetPlan())) {
            long diff = nowDate.getTime() - borrow.getLdDateRetPlan().getTime();
            overDays = (int) (diff / (1000 * 3600 * 24));
        }
        // 2. 计算金额
        if (isLost != null && isLost) {
            // 遗失：罚款为书价的3倍 (文档规则 1.3-2)
            fine = book.getBkPrice().multiply(new BigDecimal(3));
            book.setBkStatus("2"); // 状态改为遗失
        } else {
            // 正常还书：计算超期费 (文档规则 1.3-1)
            if (overDays > 0) {
                fine = type.getPunishRate().multiply(new BigDecimal(overDays));
            }
            book.setBkStatus("0"); // 状态改为在馆
        }
        // 3. 更新借阅记录
        borrow.setLdDateRetAct(nowDate);
        borrow.setLdOverDay((long) overDays);
        borrow.setLdOverMoney(fine);
        borrow.setLdPunishMoney(fine); // 默认实收等于应收
        borrow.setLsHasReturn("1");    // 标记已还
        try {
            borrow.setOperatorReturn(SecurityUtils.getUsername());
        } catch (Exception e) {
            borrow.setOperatorReturn("System");
        }
        borrowMapper.updateBorrow(borrow);
        // 4. 更新图书
        bookMapper.updateBook(book);
        // 5. 更新读者（借书量减1）
        reader.setRdBorrowQty(reader.getRdBorrowQty() - 1);
        readerMapper.updateReader(reader);
    }
}
