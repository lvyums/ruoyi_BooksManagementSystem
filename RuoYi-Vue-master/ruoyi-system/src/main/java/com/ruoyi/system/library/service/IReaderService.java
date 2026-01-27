package com.ruoyi.system.library.service;

import com.ruoyi.system.library.domain.Reader;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 读者信息Service接口
 * 
 * @author lvyu
 * @date 2025-12-14
 */
public interface IReaderService 
{
    @Transactional(rollbackFor = Exception.class) // 开启事务，任何一步报错都会回滚
    Long reissueCard(Long oldRdID);

    /**
     * 查询读者信息
     * 
     * @param rdID 读者信息主键
     * @return 读者信息
     */
    public Reader selectReaderByRdID(Long rdID);

    /**
     * 查询读者信息列表
     * 
     * @param reader 读者信息
     * @return 读者信息集合
     */
    public List<Reader> selectReaderList(Reader reader);

    /**
     * 新增读者信息
     * 
     * @param reader 读者信息
     * @return 结果
     */
    public int insertReader(Reader reader);

    /**
     * 修改读者信息
     * 
     * @param reader 读者信息
     * @return 结果
     */
    public int updateReader(Reader reader);

    /**
     * 批量删除读者信息
     * 
     * @param rdIDs 需要删除的读者信息主键集合
     * @return 结果
     */
    public int deleteReaderByRdIDs(Long[] rdIDs);

    /**
     * 删除读者信息信息
     * 
     * @param rdID 读者信息主键
     * @return 结果
     */
    public int deleteReaderByRdID(Long rdID);
}
