package com.ruoyi.system.library.service;

import com.ruoyi.system.library.domain.Readertype;

import java.util.List;

/**
 * 读者类别Service接口
 * 
 * @author lvyu
 * @date 2025-12-14
 */
public interface IReadertypeService 
{
    /**
     * 查询读者类别
     * 
     * @param rdType 读者类别主键
     * @return 读者类别
     */
    public Readertype selectReadertypeByRdType(Long rdType);

    /**
     * 查询读者类别列表
     * 
     * @param readertype 读者类别
     * @return 读者类别集合
     */
    public List<Readertype> selectReadertypeList(Readertype readertype);

    /**
     * 新增读者类别
     * 
     * @param readertype 读者类别
     * @return 结果
     */
    public int insertReadertype(Readertype readertype);

    /**
     * 修改读者类别
     * 
     * @param readertype 读者类别
     * @return 结果
     */
    public int updateReadertype(Readertype readertype);

    /**
     * 批量删除读者类别
     * 
     * @param rdTypes 需要删除的读者类别主键集合
     * @return 结果
     */
    public int deleteReadertypeByRdTypes(Long[] rdTypes);

    /**
     * 删除读者类别信息
     * 
     * @param rdType 读者类别主键
     * @return 结果
     */
    public int deleteReadertypeByRdType(Long rdType);
}
