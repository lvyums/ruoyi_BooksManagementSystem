package com.ruoyi.system.library.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.library.domain.Readertype;
import com.ruoyi.system.library.service.IReadertypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 读者类别Controller
 * 
 * @author lvyu
 * @date 2025-12-14
 */
@RestController
@RequestMapping("/library/readertype")
public class ReadertypeController extends BaseController
{
    @Autowired
    private IReadertypeService readertypeService;

    /**
     * 查询读者类别列表
     */
    @PreAuthorize("@ss.hasPermi('library:readertype:list')")
    @GetMapping("/list")
    public TableDataInfo list(Readertype readertype)
    {
        startPage();
        List<Readertype> list = readertypeService.selectReadertypeList(readertype);
        return getDataTable(list);
    }

    /**
     * 导出读者类别列表
     */
    @PreAuthorize("@ss.hasPermi('library:readertype:export')")
    @Log(title = "读者类别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Readertype readertype)
    {
        List<Readertype> list = readertypeService.selectReadertypeList(readertype);
        ExcelUtil<Readertype> util = new ExcelUtil<Readertype>(Readertype.class);
        util.exportExcel(response, list, "读者类别数据");
    }

    /**
     * 获取读者类别详细信息
     */
    @PreAuthorize("@ss.hasPermi('library:readertype:query')")
    @GetMapping(value = "/{rdType}")
    public AjaxResult getInfo(@PathVariable("rdType") Long rdType)
    {
        return success(readertypeService.selectReadertypeByRdType(rdType));
    }

    /**
     * 新增读者类别
     */
    @PreAuthorize("@ss.hasPermi('library:readertype:add')")
    @Log(title = "读者类别", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Readertype readertype)
    {
        return toAjax(readertypeService.insertReadertype(readertype));
    }

    /**
     * 修改读者类别
     */
    @PreAuthorize("@ss.hasPermi('library:readertype:edit')")
    @Log(title = "读者类别", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Readertype readertype)
    {
        return toAjax(readertypeService.updateReadertype(readertype));
    }

    /**
     * 删除读者类别
     */
    @PreAuthorize("@ss.hasPermi('library:readertype:remove')")
    @Log(title = "读者类别", businessType = BusinessType.DELETE)
	@DeleteMapping("/{rdTypes}")
    public AjaxResult remove(@PathVariable Long[] rdTypes)
    {
        return toAjax(readertypeService.deleteReadertypeByRdTypes(rdTypes));
    }
}
