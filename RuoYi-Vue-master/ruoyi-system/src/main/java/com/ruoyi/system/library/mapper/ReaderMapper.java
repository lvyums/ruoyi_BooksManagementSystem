package com.ruoyi.system.library.mapper;

import com.ruoyi.system.library.domain.Reader;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 读者信息Mapper接口
 * 
 * @author lvyu
 * @date 2025-12-14
 */
@Mapper // 2. 务必加上这个注解
public interface ReaderMapper 
{
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
     * 删除读者信息
     * 
     * @param rdID 读者信息主键
     * @return 结果
     */
    public int deleteReaderByRdID(Long rdID);

    /**
     * 批量删除读者信息
     * 
     * @param rdIDs 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteReaderByRdIDs(Long[] rdIDs);
}
