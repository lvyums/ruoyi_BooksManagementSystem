package com.ruoyi.system.library.mapper;

import com.ruoyi.system.library.domain.Book;

import java.util.List;

/**
 * 图书信息Mapper接口
 * 
 * @author lvyu
 * @date 2025-12-14
 */
public interface BookMapper 
{
    /**
     * 查询图书信息
     *
     * @param bkID 图书信息主键
     * @return 图书信息
     */
    public com.ruoyi.system.library.domain.Book selectBookByBkID(Long bkID);

    /**
     * 查询图书信息列表
     * 
     * @param book 图书信息
     * @return 图书信息集合
     */
    public List<Book> selectBookList(Book book);

    /**
     * 新增图书信息
     * 
     * @param book 图书信息
     * @return 结果
     */
    public int insertBook(Book book);

    /**
     * 修改图书信息
     * 
     * @param book 图书信息
     * @return 结果
     */
    public int updateBook(Book book);

    /**
     * 删除图书信息
     * 
     * @param bkID 图书信息主键
     * @return 结果
     */
    public int deleteBookByBkID(Long bkID);

    /**
     * 批量删除图书信息
     * 
     * @param bkIDs 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBookByBkIDs(Long[] bkIDs);
}
