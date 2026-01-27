package com.ruoyi.system.library.controller;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.system.service.ISysUserService;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.library.domain.Reader;
import com.ruoyi.system.library.service.IReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;

// 引入 ExcelUtil
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.web.multipart.MultipartFile;

/**
 * 读者信息Controller
 * 
 * @author lvyu
 * @date 2025-12-14
 */
@RestController
@RequestMapping("/library/reader")
public class ReaderController extends BaseController
{
    @Autowired
    private IReaderService readerService;
    @Autowired
    private ISysUserService sysUserService; // 注入用户服务



    /**
     * 查询读者信息列表
     */
    @PreAuthorize("@ss.hasPermi('library:reader:list')")
    @GetMapping("/list")
    public TableDataInfo list(Reader reader)
    {
        startPage();
        List<Reader> list = readerService.selectReaderList(reader);
        return getDataTable(list);
    }

    /**
     * 导出读者信息列表
     */
    @PreAuthorize("@ss.hasPermi('library:reader:export')")
    @Log(title = "读者信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Reader reader)
    {
        List<Reader> list = readerService.selectReaderList(reader);
        ExcelUtil<Reader> util = new ExcelUtil<Reader>(Reader.class);
        util.exportExcel(response, list, "读者信息数据");
    }

    /**
     * 获取读者信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('library:reader:query')")
    @GetMapping(value = "/{rdID}")
    public AjaxResult getInfo(@PathVariable("rdID") Long rdID)
    {
        return success(readerService.selectReaderByRdID(rdID));
    }

    /**
     * 新增读者信息
     */
    @PreAuthorize("@ss.hasPermi('library:reader:add')")
    @Log(title = "读者信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Reader reader)
    {
        return toAjax(readerService.insertReader(reader));
    }

    /**
     * 修改读者信息
     */
    @PreAuthorize("@ss.hasPermi('library:reader:edit')")
    @Log(title = "读者信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Reader reader)
    {
        return toAjax(readerService.updateReader(reader));
    }

    /**
     * 删除读者信息
     */
    @PreAuthorize("@ss.hasPermi('library:reader:remove')")
    @Log(title = "读者信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{rdIDs}")
    public AjaxResult remove(@PathVariable Long[] rdIDs)
    {
        return toAjax(readerService.deleteReaderByRdIDs(rdIDs));
    }

    /**
     * 导入读者数据
     */
    @Log(title = "读者管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('library:reader:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<Reader> util = new ExcelUtil<Reader>(Reader.class);
        List<Reader> readerList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = "";

        int successNum = 0;
        for (Reader reader : readerList)
        {
            try {
                // 调用 service 的 insert自动触发 syncToSysUser 创建账号
                readerService.insertReader(reader);
                successNum++;
            } catch (Exception e) {
            }
        }
        return AjaxResult.success("成功导入 " + successNum + " 条数据");
    }

    /**
     * 下载导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Reader> util = new ExcelUtil<Reader>(Reader.class);
        util.importTemplateExcel(response, "读者数据");
    }

    /**
     * 补办借书证
     */
    @PreAuthorize("@ss.hasPermi('library:reader:edit')")
    @PostMapping("/reissue/{rdID}")
    public AjaxResult reissue(@PathVariable("rdID") Long rdID) {
        Long newRdID = readerService.reissueCard(rdID);
        return AjaxResult.success("补办成功！原卡已注销，新借书证号为：" + newRdID);
    }







    /**
     * 【核按钮】批量重置所有用户密码为 123456
     * 访问地址：http://localhost:8080/library/reader/fixAllPwd
     * 注意：运行完一次后建议删除或注释掉！
     */
//    @Anonymous
//    @GetMapping("/fixAllPwd")
//    public AjaxResult fixAllPwd()
//    {
//        // === 第一步：伪造一个 Admin 身份，骗过 SecurityUtils ===
//        try {
//            SysUser adminUser = new SysUser();
//            adminUser.setUserId(1L);
//            adminUser.setUserName("admin");
//            // 创建一个虚拟的登录用户
//            LoginUser loginUser = new LoginUser(1L, 100L, adminUser, new HashSet<>());
//            // 将其放入安全上下文
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        } catch (Exception e) {
//            // 忽略伪造身份的错误，尽力而为
//        }
//
//        // === 第二步：执行重置逻辑 ===
//        // 1. 查询所有用户
//        SysUser query = new SysUser();
//        List<SysUser> list = sysUserService.selectUserList(query);
//
//        int count = 0;
//        int errorCount = 0;
//
//        for (SysUser user : list) {
//            try {
//                String username = user.getUserName();
//
//                // === 【核心修复】 ===
//                // 如果账号不是纯数字 (比如 "admin", "ry")，直接跳过，不重置！
//                if (!username.matches("\\d+")) {
//                    System.out.println("跳过非数字账号: " + username);
//                    continue;
//                }
//
//                // 执行重置
//                sysUserService.resetUserPwd(Long.valueOf(username), "123456");
//                count++;
//
//            } catch (Exception ex) {
//                // 捕获单个用户的错误，不影响其他人
//                errorCount++;
//                System.err.println("用户 [" + user.getUserName() + "] 重置失败: " + ex.getMessage());
//            }
//        }
//
//        return success("操作完成！成功重置 " + count + " 人，跳过/失败 " + errorCount + " 人。请尝试用 123456 登录读者账号。");
//    }
}
