package com.ruoyi.system.library.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 借阅记录对象 tb_borrow
 * 
 * @author lvyu
 * @date 2025-12-14
 */
public class Borrow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 借阅流水号 */
    private Long BorrowID;

    /** 新增：图书名称（用于前端显示） */
    private String bookName;

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }

    /** 读者ID */
    @Excel(name = "读者ID")
    private Long rdID;

    /** 图书ID */
    @Excel(name = "图书ID")
    private Long bkID;

    /** 续借次数 */
    @Excel(name = "续借次数")
    private Long ldContinueTimes;

    /** 借书日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "借书日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date ldDateOut;

    /** 应还日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "应还日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date ldDateRetPlan;

    /** 实际还书日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际还书日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date ldDateRetAct;

    /** 超期天数 */
    @Excel(name = "超期天数")
    private Long ldOverDay;

    /** 超期金额 */
    @Excel(name = "超期金额")
    private BigDecimal ldOverMoney;

    /** 罚款金额 */
    @Excel(name = "罚款金额")
    private BigDecimal ldPunishMoney;

    /** 是否归还(0未还 1已还) */
    @Excel(name = "是否归还(0未还 1已还)")
    private String lsHasReturn;

    /** 借书操作员 */
    @Excel(name = "借书操作员")
    private String OperatorBorrow;

    /** 还书操作员 */
    @Excel(name = "还书操作员")
    private String OperatorReturn;

    public void setBorrowID(Long BorrowID) 
    {
        this.BorrowID = BorrowID;
    }

    public Long getBorrowID() 
    {
        return BorrowID;
    }

    public void setRdID(Long rdID) 
    {
        this.rdID = rdID;
    }

    public Long getRdID() 
    {
        return rdID;
    }

    public void setBkID(Long bkID) 
    {
        this.bkID = bkID;
    }

    public Long getBkID() 
    {
        return bkID;
    }

    public void setLdContinueTimes(Long ldContinueTimes) 
    {
        this.ldContinueTimes = ldContinueTimes;
    }

    public Long getLdContinueTimes() 
    {
        return ldContinueTimes;
    }

    public void setLdDateOut(Date ldDateOut) 
    {
        this.ldDateOut = ldDateOut;
    }

    public Date getLdDateOut() 
    {
        return ldDateOut;
    }

    public void setLdDateRetPlan(Date ldDateRetPlan) 
    {
        this.ldDateRetPlan = ldDateRetPlan;
    }

    public Date getLdDateRetPlan() 
    {
        return ldDateRetPlan;
    }

    public void setLdDateRetAct(Date ldDateRetAct) 
    {
        this.ldDateRetAct = ldDateRetAct;
    }

    public Date getLdDateRetAct() 
    {
        return ldDateRetAct;
    }

    public void setLdOverDay(Long ldOverDay) 
    {
        this.ldOverDay = ldOverDay;
    }

    public Long getLdOverDay() 
    {
        return ldOverDay;
    }

    public void setLdOverMoney(BigDecimal ldOverMoney) 
    {
        this.ldOverMoney = ldOverMoney;
    }

    public BigDecimal getLdOverMoney() 
    {
        return ldOverMoney;
    }

    public void setLdPunishMoney(BigDecimal ldPunishMoney) 
    {
        this.ldPunishMoney = ldPunishMoney;
    }

    public BigDecimal getLdPunishMoney() 
    {
        return ldPunishMoney;
    }

    public void setLsHasReturn(String lsHasReturn) 
    {
        this.lsHasReturn = lsHasReturn;
    }

    public String getLsHasReturn() 
    {
        return lsHasReturn;
    }

    public void setOperatorBorrow(String OperatorBorrow) 
    {
        this.OperatorBorrow = OperatorBorrow;
    }

    public String getOperatorBorrow() 
    {
        return OperatorBorrow;
    }

    public void setOperatorReturn(String OperatorReturn) 
    {
        this.OperatorReturn = OperatorReturn;
    }

    public String getOperatorReturn() 
    {
        return OperatorReturn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("BorrowID", getBorrowID())
            .append("rdID", getRdID())
            .append("bkID", getBkID())
            .append("ldContinueTimes", getLdContinueTimes())
            .append("ldDateOut", getLdDateOut())
            .append("ldDateRetPlan", getLdDateRetPlan())
            .append("ldDateRetAct", getLdDateRetAct())
            .append("ldOverDay", getLdOverDay())
            .append("ldOverMoney", getLdOverMoney())
            .append("ldPunishMoney", getLdPunishMoney())
            .append("lsHasReturn", getLsHasReturn())
            .append("OperatorBorrow", getOperatorBorrow())
            .append("OperatorReturn", getOperatorReturn())
            .toString();
    }
}
