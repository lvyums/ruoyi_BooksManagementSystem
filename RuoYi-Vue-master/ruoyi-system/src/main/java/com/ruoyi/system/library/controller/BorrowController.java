package com.ruoyi.system.library.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.library.domain.Borrow;
import com.ruoyi.system.library.service.IBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 借阅记录Controller
 * 
 * @author lvyu
 * @date 2025-12-14
 */
@RestController
@RequestMapping("/library/borrow")
public class BorrowController extends BaseController
{
    @Autowired
    private IBorrowService borrowService;

    /**
     * 查询借阅记录列表
     */
    @PreAuthorize("@ss.hasPermi('library:borrow:list')")
    @GetMapping("/list")
    public TableDataInfo list(Borrow borrow)
    {
        startPage();
        List<Borrow> list = borrowService.selectBorrowList(borrow);
        return getDataTable(list);
    }

    /**
     * 导出借阅记录列表
     */
    @PreAuthorize("@ss.hasPermi('library:borrow:export')")
    @Log(title = "借阅记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Borrow borrow)
    {
        List<Borrow> list = borrowService.selectBorrowList(borrow);
        ExcelUtil<Borrow> util = new ExcelUtil<Borrow>(Borrow.class);
        util.exportExcel(response, list, "借阅记录数据");
    }

    /**
     * 获取借阅记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('library:borrow:query')")
    @GetMapping(value = "/{BorrowID}")
    public AjaxResult getInfo(@PathVariable("BorrowID") Long BorrowID)
    {
        return success(borrowService.selectBorrowByBorrowID(BorrowID));
    }

    /**
     * 新增借阅记录
     */
    @PreAuthorize("@ss.hasPermi('library:borrow:add')")
    @Log(title = "借阅记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Borrow borrow)
    {
        return toAjax(borrowService.insertBorrow(borrow));
    }

    /**
     * 修改借阅记录
     */
    @PreAuthorize("@ss.hasPermi('library:borrow:edit')")
    @Log(title = "借阅记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Borrow borrow)
    {
        return toAjax(borrowService.updateBorrow(borrow));
    }

    /**
     * 删除借阅记录
     */
    @PreAuthorize("@ss.hasPermi('library:borrow:remove')")
    @Log(title = "借阅记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{BorrowIDs}")
    public AjaxResult remove(@PathVariable Long[] BorrowIDs)
    {
        return toAjax(borrowService.deleteBorrowByBorrowIDs(BorrowIDs));
    }

    // =========================================================================
    //                            新增核心业务接口
    // =========================================================================

    /**
     * 业务功能：办理借书
     * URL: POST /library/borrow/lend?rdID=1&bkID=2
     */
    @PreAuthorize("@ss.hasPermi('library:borrow:add')") // 复用新增权限
    @Log(title = "借阅管理-借书", businessType = BusinessType.INSERT)
    @PostMapping("/lend")
    public AjaxResult lend(@RequestParam Long rdID, @RequestParam Long bkID)
    {
        borrowService.borrowBook(rdID, bkID);
        return success("借书成功！");
    }

    /**
     * 业务功能：办理续借
     * URL: POST /library/borrow/renew/101
     */
    @PreAuthorize("@ss.hasPermi('library:borrow:edit')") // 复用修改权限
    @Log(title = "借阅管理-续借", businessType = BusinessType.UPDATE)
    @PostMapping("/renew/{borrowID}")
    public AjaxResult renew(@PathVariable Long borrowID)
    {
        borrowService.renewBook(borrowID);
        return success("续借成功！");
    }

    /**
     * 业务功能：办理还书
     * URL: POST /library/borrow/return?borrowID=101&isLost=false
     */
    @PreAuthorize("@ss.hasPermi('library:borrow:edit')") // 复用修改权限
    @Log(title = "借阅管理-还书", businessType = BusinessType.UPDATE)
    @PostMapping("/return")
    public AjaxResult returnBook(@RequestParam Long borrowID,
                                 @RequestParam(required=false, defaultValue="false") Boolean isLost)
    {
        borrowService.returnBook(borrowID, isLost);
        return success("还书成功！");
    }
}

