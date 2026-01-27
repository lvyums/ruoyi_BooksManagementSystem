package com.ruoyi.system.library.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.library.domain.Book;
import com.ruoyi.system.library.mapper.BookMapper;
import com.ruoyi.system.library.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 图书信息Service业务层处理
 *
 * @author lvyu
 * @date 2025-12-14
 */
@Service
public class BookServiceImpl implements IBookService
{
    @Autowired
    private BookMapper bookMapper;

    /**
     * 查询图书信息
     *
     * @param bkID 图书信息主键
     * @return 图书信息
     */
    @Override
    public Book selectBookByBkID(Long bkID)
    {
        return bookMapper.selectBookByBkID(bkID);
    }

    /**
     * 查询图书信息列表
     *
     * @param book 图书信息
     * @return 图书信息
     */
    @Override
    public List<Book> selectBookList(Book book)
    {
        return bookMapper.selectBookList(book);
    }

    /**
     * 新增图书信息
     * 业务规则：
     * 1. 默认状态设为 0 (在馆)
     * 2. 如果未填写入馆日期，默认为当前日期
     *
     * @param book 图书信息
     * @return 结果
     */
    @Override
    public int insertBook(Book book)
    {
        if (book.getBkStatus() == null) {
            book.setBkStatus("0"); // 0=在馆
        }
        if (book.getBkDateIn() == null) {
            book.setBkDateIn(DateUtils.getNowDate());
        }
        return bookMapper.insertBook(book);
    }

    /**
     * 修改图书信息
     * 业务规则：
     * 1. 如果要变卖(3)或销毁(4)，必须确保图书当前不在借出状态(1)
     *
     * @param book 图书信息
     * @return 结果
     */
    @Override
    public int updateBook(Book book)
    {
        // 检查特殊状态变更
        String newStatus = book.getBkStatus();
        // 3=变卖, 4=销毁
        if ("3".equals(newStatus) || "4".equals(newStatus)) {
            Book dbBook = bookMapper.selectBookByBkID(book.getBkID());
            // 1=借出
            if (dbBook != null && "1".equals(dbBook.getBkStatus())) {
                throw new ServiceException("该图书当前处于借出状态，无法进行变卖或销毁处理！");
            }
        }
        return bookMapper.updateBook(book);
    }

    /**
     * 批量删除图书信息
     * 业务规则：借出状态的图书不能删除
     *
     * @param bkIDs 需要删除的图书信息主键
     * @return 结果
     */
    @Override
    public int deleteBookByBkIDs(Long[] bkIDs)
    {
        for (Long bkID : bkIDs) {
            Book book = bookMapper.selectBookByBkID(bkID);
            if (book != null && "1".equals(book.getBkStatus())) {
                throw new ServiceException("图书[" + book.getBkName() + "]正在借出中，无法删除！");
            }
        }
        return bookMapper.deleteBookByBkIDs(bkIDs);
    }

    /**
     * 删除图书信息信息
     *
     * @param bkID 图书信息主键
     * @return 结果
     */
    @Override
    public int deleteBookByBkID(Long bkID)
    {
        Book book = bookMapper.selectBookByBkID(bkID);
        if (book != null && "1".equals(book.getBkStatus())) {
            throw new ServiceException("该图书正在借出中，无法删除！");
        }
        return bookMapper.deleteBookByBkID(bkID);
    }
}