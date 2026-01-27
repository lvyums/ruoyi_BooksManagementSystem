package com.ruoyi.system.library.service;

import com.ruoyi.system.library.domain.Borrow;

import java.util.List;

/**
 * 借阅记录Service接口
 * 
 * @author lvyu
 * @date 2025-12-14
 */
public interface IBorrowService 
{
    /**
     * 办理借书
     * @param rdID 读者ID
     * @param bkID 图书ID
     */
    public void borrowBook(Long rdID, Long bkID);

    /**
     * 办理续借
     * @param borrowID 借阅记录ID
     */
    public void renewBook(Long borrowID);

    /**
     * 办理还书
     * @param borrowID 借阅记录ID
     * @param isLost 是否遗失
     */
    public void returnBook(Long borrowID, Boolean isLost);
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
     * 批量删除借阅记录
     * 
     * @param BorrowIDs 需要删除的借阅记录主键集合
     * @return 结果
     */
    public int deleteBorrowByBorrowIDs(Long[] BorrowIDs);

    /**
     * 删除借阅记录信息
     * 
     * @param BorrowID 借阅记录主键
     * @return 结果
     */
    public int deleteBorrowByBorrowID(Long BorrowID);
}
