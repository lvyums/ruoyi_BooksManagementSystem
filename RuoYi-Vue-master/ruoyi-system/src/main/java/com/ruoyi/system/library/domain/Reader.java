package com.ruoyi.system.library.domain;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 读者信息对象 tb_reader
 * 
 * @author lvyu
 * @date 2025-12-14
 */
public class Reader extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 借书证号 */
    private Long rdID;

    /** 姓名 */
    @Excel(name = "姓名")
    private String rdName;

    /** 性别(0男 1女) */
    @Excel(name = "性别(0男 1女)")
    private String rdSex;

    /** 读者类别 */
    @Excel(name = "读者类别")
    private Long rdType;

    /** 单位/班级 */
    @Excel(name = "单位/班级")
    private String rdDept;

    /** 电话 */
    @Excel(name = "电话")
    private String rdPhone;

    /** 电子邮箱 */
    @Excel(name = "电子邮箱")
    private String rdEmail;

    /** 办证日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "办证日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date rdDateReg;

    /** 照片 */
    @Excel(name = "照片")
    private String rdPhoto;

    /** 状态(0有效 1挂失 2注销) */
    @Excel(name = "状态(0有效 1挂失 2注销)")
    private String rdStatus;

    /** 已借书数量 */
    @Excel(name = "已借书数量")
    private Long rdBorrowQty;

    /** 密码 */
    @Excel(name = "密码")
    private String rdPwd;

    /** 管理角色 */
    @Excel(name = "管理角色")
    private Long rdAdminRoles;

    public void setRdID(Long rdID) 
    {
        this.rdID = rdID;
    }

    public Long getRdID() 
    {
        return rdID;
    }

    public void setRdName(String rdName) 
    {
        this.rdName = rdName;
    }

    public String getRdName() 
    {
        return rdName;
    }

    public void setRdSex(String rdSex) 
    {
        this.rdSex = rdSex;
    }

    public String getRdSex() 
    {
        return rdSex;
    }

    public void setRdType(Long rdType) 
    {
        this.rdType = rdType;
    }

    public Long getRdType() 
    {
        return rdType;
    }

    public void setRdDept(String rdDept) 
    {
        this.rdDept = rdDept;
    }

    public String getRdDept() 
    {
        return rdDept;
    }

    public void setRdPhone(String rdPhone) 
    {
        this.rdPhone = rdPhone;
    }

    public String getRdPhone() 
    {
        return rdPhone;
    }

    public void setRdEmail(String rdEmail) 
    {
        this.rdEmail = rdEmail;
    }

    public String getRdEmail() 
    {
        return rdEmail;
    }

    public void setRdDateReg(Date rdDateReg) 
    {
        this.rdDateReg = rdDateReg;
    }

    public Date getRdDateReg() 
    {
        return rdDateReg;
    }

    public void setRdPhoto(String rdPhoto) 
    {
        this.rdPhoto = rdPhoto;
    }

    public String getRdPhoto() 
    {
        return rdPhoto;
    }

    public void setRdStatus(String rdStatus) 
    {
        this.rdStatus = rdStatus;
    }

    public String getRdStatus() 
    {
        return rdStatus;
    }

    public void setRdBorrowQty(Long rdBorrowQty) 
    {
        this.rdBorrowQty = rdBorrowQty;
    }

    public Long getRdBorrowQty() 
    {
        return rdBorrowQty;
    }

    public void setRdPwd(String rdPwd) 
    {
        this.rdPwd = rdPwd;
    }

    public String getRdPwd() 
    {
        return rdPwd;
    }

    public void setRdAdminRoles(Long rdAdminRoles) 
    {
        this.rdAdminRoles = rdAdminRoles;
    }

    public Long getRdAdminRoles() 
    {
        return rdAdminRoles;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("rdID", getRdID())
            .append("rdName", getRdName())
            .append("rdSex", getRdSex())
            .append("rdType", getRdType())
            .append("rdDept", getRdDept())
            .append("rdPhone", getRdPhone())
            .append("rdEmail", getRdEmail())
            .append("rdDateReg", getRdDateReg())
            .append("rdPhoto", getRdPhoto())
            .append("rdStatus", getRdStatus())
            .append("rdBorrowQty", getRdBorrowQty())
            .append("rdPwd", getRdPwd())
            .append("rdAdminRoles", getRdAdminRoles())
            .toString();
    }
}
