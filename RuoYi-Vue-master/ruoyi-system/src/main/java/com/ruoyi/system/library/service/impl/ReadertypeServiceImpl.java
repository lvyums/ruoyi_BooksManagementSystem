package com.ruoyi.system.library.service.impl;

import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.library.mapper.ReaderMapper;
import com.ruoyi.system.library.mapper.ReadertypeMapper;
import com.ruoyi.system.library.domain.Reader;
import com.ruoyi.system.library.domain.Readertype;
import com.ruoyi.system.library.service.IReadertypeService;

import java.util.List;

/**
 * 读者类别Service业务层处理
 * 
 * @author lvyu
 * @date 2025-12-14
 */
@Service
public class ReadertypeServiceImpl implements IReadertypeService 
{
    @Autowired
    private ReadertypeMapper readertypeMapper;

    // 需要注入ReaderMapper用于删除时的关联检查
    @Autowired
    private ReaderMapper readerMapper;

    /**
     * 查询读者类别
     *
     * @param rdType 读者类别主键
     * @return 读者类别
     */
    @Override
    public Readertype selectReadertypeByRdType(Long rdType)
    {
        return readertypeMapper.selectReadertypeByRdType(rdType);
    }

    /**
     * 查询读者类别列表
     * 
     * @param readertype 读者类别
     * @return 读者类别
     */
    @Override
    public List<Readertype> selectReadertypeList(Readertype readertype)
    {
        return readertypeMapper.selectReadertypeList(readertype);
    }

    /**
     * 新增读者类别
     * 
     * @param readertype 读者类别
     * @return 结果
     */
    @Override
    public int insertReadertype(Readertype readertype)
    {
        return readertypeMapper.insertReadertype(readertype);
    }

    /**
     * 修改读者类别
     * 
     * @param readertype 读者类别
     * @return 结果
     */
    @Override
    public int updateReadertype(Readertype readertype)
    {
        return readertypeMapper.updateReadertype(readertype);
    }

    /**
     * 批量删除读者类别
     * 
     * @param rdTypes 需要删除的读者类别主键
     * @return 结果
     */
    @Override
    public int deleteReadertypeByRdTypes(Long[] rdTypes)
    {
        for (Long rdType : rdTypes) {
            // 1. 查询该类别信息用于报错提示
            Readertype type = readertypeMapper.selectReadertypeByRdType(rdType);

            // 2. 检查是否有读者属于该类别
            Reader readerParam = new Reader();
            // 注意：Reader实体中的rdType类型通常为Long，如果是Integer需强转
            // 假设 reader.setRdType 接受 Long
            if (rdType != null) {
                // 这里假设Reader实体里rdType是Long类型。如果报错请改为 readerParam.setRdType(rdType.intValue());
                readerParam.setRdType(rdType);
            }

            List<Reader> readerList = readerMapper.selectReaderList(readerParam);

            if (readerList.size() > 0) {
                String typeName = (type != null) ? type.getRdTypeName() : rdType.toString();
                throw new ServiceException("读者类别 [" + typeName + "] 下已存在 " + readerList.size() + " 名读者，无法删除！请先变更这些读者的类别。");
            }
        }
        return readertypeMapper.deleteReadertypeByRdTypes(rdTypes);
    }

    /**
     * 删除读者类别信息
     * 
     * @param rdType 读者类别主键
     * @return 结果
     */
    @Override
    public int deleteReadertypeByRdType(Long rdType)
    {
        return readertypeMapper.deleteReadertypeByRdType(rdType);
    }
}
