package com.aisino.admin.companyCard.codeAssign.bean;

import com.aisino.admin.global.utils.DateUtils;

import java.util.Date;

public class KPAssignCodeVO {
    private Integer detailId;
    private Integer mainId;
    private String preAssignCode;
    private String bindCompanyName;
    private String bindCompanyTaxid;
    private String bindPerson;
    private Date bindTime;
    private String originCode;
    private String status;

    private String assignCompanyName;
    private String assignCompanyTaxid;
    private String assignEmployeeId;
    private String assignEmployeeName;
    private String assignEmployeeMobile;
    private Integer assignAmount;
    private String createPerson;
    private Date createTime;
    private String modifyPerson;
    private Date modifyTime;

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public Integer getMainId() {
        return mainId;
    }

    public void setMainId(Integer mainId) {
        this.mainId = mainId;
    }

    public String getPreAssignCode() {
        return preAssignCode;
    }

    public void setPreAssignCode(String preAssignCode) {
        this.preAssignCode = preAssignCode;
    }

    public String getBindCompanyName() {
        return bindCompanyName;
    }

    public void setBindCompanyName(String bindCompanyName) {
        this.bindCompanyName = bindCompanyName;
    }

    public String getBindCompanyTaxid() {
        return bindCompanyTaxid;
    }

    public void setBindCompanyTaxid(String bindCompanyTaxid) {
        this.bindCompanyTaxid = bindCompanyTaxid;
    }

    public String getBindPerson() {
        return bindPerson;
    }

    public void setBindPerson(String bindPerson) {
        this.bindPerson = bindPerson;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignCompanyName() {
        return assignCompanyName;
    }

    public void setAssignCompanyName(String assignCompanyName) {
        this.assignCompanyName = assignCompanyName;
    }

    public String getAssignCompanyTaxid() {
        return assignCompanyTaxid;
    }

    public void setAssignCompanyTaxid(String assignCompanyTaxid) {
        this.assignCompanyTaxid = assignCompanyTaxid;
    }

    public String getAssignEmployeeId() {
        return assignEmployeeId;
    }

    public void setAssignEmployeeId(String assignEmployeeId) {
        this.assignEmployeeId = assignEmployeeId;
    }

    public String getAssignEmployeeName() {
        return assignEmployeeName;
    }

    public void setAssignEmployeeName(String assignEmployeeName) {
        this.assignEmployeeName = assignEmployeeName;
    }

    public String getAssignEmployeeMobile() {
        return assignEmployeeMobile;
    }

    public void setAssignEmployeeMobile(String assignEmployeeMobile) {
        this.assignEmployeeMobile = assignEmployeeMobile;
    }

    public Integer getAssignAmount() {
        return assignAmount;
    }

    public void setAssignAmount(Integer assignAmount) {
        this.assignAmount = assignAmount;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyPerson() {
        return modifyPerson;
    }

    public void setModifyPerson(String modifyPerson) {
        this.modifyPerson = modifyPerson;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCreateTimeStr(){
        String str = null;
        if (createTime!=null) {
            str = DateUtils.format(createTime);
        }
        return str;
    }
    public String getBindTimeStr(){
        String str = null;
        if (bindTime!=null) {
            str = DateUtils.format(bindTime);
        }
        return str;
    }
    public String getModifyTimeStr(){
        String str = null;
        if (modifyTime!=null) {
            str = DateUtils.format(modifyTime);
        }
        return str;
    }

    public String getStatusStr() {
        String str = "新建 ";
        if (status != null) {
            if (status.equals("0")) {
                str = "新建 ";
            } else if (status.equals("1")) {
                str = "已绑定  ";
            } else if (status.equals("2")) {
                str = "已恢复 ";
            }
        }
        return str;
    }

    public String getAssignCompanyNameTaxid(){
        String str = assignCompanyName;
        if(assignCompanyTaxid!=null&&assignCompanyTaxid.trim().length()>0) {
            str += "<br />(" + assignCompanyTaxid + ")";
        }
        return str;
    }

    public String getBindCompanyNameTaxid(){
        String str = bindCompanyName;
        if(bindCompanyTaxid!=null&&bindCompanyTaxid.trim().length()>0) {
            str += "<br />(" + bindCompanyTaxid + ")";
        }
        return str;
    }
    public String getAssignEmployeeNameId(){
        String str = assignEmployeeName;
        if(assignEmployeeId!=null&&assignEmployeeId.trim().length()>0) {
            str += "<br />(" + assignCompanyTaxid + ")";
        }
        return str;
    }

    public String getCreatePersonTime(){
        String str = createPerson;
        if(getCreateTimeStr()!=null) {
            str += "<br />(" + getCreateTimeStr() + ")";
        }
        return str;
    }
    
    public String getBindPersonTime(){
        String str = bindPerson;
        if(getBindTimeStr()!=null) {
            str += "<br />(" + getBindTimeStr() + ")";
        }
        return str;
    }

    public String getModifyPersonTime(){
        String str = modifyPerson;
        if(getModifyTimeStr()!=null) {
            str += "<br />(" + getModifyTimeStr() + ")";
        }
        return str;
    }

    @Override
    public String toString() {
        return "KPAssignCodeVO{" +
                "detailId=" + detailId +
                ", mainId=" + mainId +
                ", preAssignCode='" + preAssignCode + '\'' +
                ", bindCompanyName='" + bindCompanyName + '\'' +
                ", bindCompanyTaxid='" + bindCompanyTaxid + '\'' +
                ", bindPerson='" + bindPerson + '\'' +
                ", bindTime=" + bindTime +
                ", originCode='" + originCode + '\'' +
                ", status='" + status + '\'' +
                ", assignCompanyName='" + assignCompanyName + '\'' +
                ", assignCompanyTaxid='" + assignCompanyTaxid + '\'' +
                ", assignEmployeeId='" + assignEmployeeId + '\'' +
                ", assignEmployeeName='" + assignEmployeeName + '\'' +
                ", assignEmployeeMobile='" + assignEmployeeMobile + '\'' +
                ", assignAmount=" + assignAmount +
                ", createPerson='" + createPerson + '\'' +
                ", createTime=" + createTime +
                ", modifyPerson='" + modifyPerson + '\'' +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
