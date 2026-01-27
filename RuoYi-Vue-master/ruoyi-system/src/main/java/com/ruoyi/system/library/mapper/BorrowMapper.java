package com.ruoyi.system.library.mapper;
import org.apache.ibatis.annotations.Param; // 记得导包
import com.ruoyi.system.library.domain.Borrow;

import java.util.List;

/**
 * 借阅记录Mapper接口
 * 
 * @author lvyu
 * @date 2025-12-14
 */
public interface BorrowMapper 
{
    /**
     * 查询借阅记录
     * 
     * @param BorrowID 借阅记录主键
     * @return 借阅记录
     */
    public Borrow selectBorrowByBorrowID(Long BorrowID);

    /**
     * 查询借阅记录列表
     * 
     * @param borrow 借阅记录
     * @return 借阅记录集合
     */
    public List<Borrow> selectBorrowList(Borrow borrow);

    /**
     * 新增借阅记录
     * 
     * @param borrow 借阅记录
     * @return 结果
     */
    public int insertBorrow(Borrow borrow);

    /**
     * 修改借阅记录
     * 
     * @param borrow 借阅记录
     * @return 结果
     */
    public int updateBorrow(Borrow borrow);

    /**
     * 删除借阅记录
     * 
     * @param BorrowID 借阅记录主键
     * @return 结果
     */
    public int deleteBorrowByBorrowID(Long BorrowID);

    /**
     * 批量删除借阅记录
     * 
     * @param BorrowIDs 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBorrowByBorrowIDs(Long[] BorrowIDs);

    /**
     * 转移借阅记录（将旧读者的记录转移给新读者）
     * @param oldRdID 旧读者ID
     * @param newRdID 新读者ID
     * @return 影响行数
     */
    public int transferBorrowRecords(@Param("oldRdID") Long oldRdID, @Param("newRdID") Long newRdID);

    /**
     * 查询某读者的超期未还数量
     * @param rdID 读者ID
     * @return 超期数量
     */
    public int countOverdueByRdID(Long rdID);


}
