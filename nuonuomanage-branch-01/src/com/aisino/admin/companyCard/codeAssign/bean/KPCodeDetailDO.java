package com.aisino.admin.companyCard.codeAssign.bean;

import java.util.Date;

public class KPCodeDetailDO {
    private Integer detailId;
    private Integer mainId;
    private String preAssignCode;
    private String assignEmployeeId;
    private String assignEmployeeName;
    private String assignEmployeeMobile;
    private String bindCompanyName;
    private String bindCompanyTaxid;
    private String bindPerson;
    private Date bindTime;
    private String originCode;
    private String status;

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

    @Override
    public String toString() {
        return "KPCodeDetailDO{" +
                "detailId=" + detailId +
                ", mainId=" + mainId +
                ", preAssignCode='" + preAssignCode + '\'' +
                ", assignEmployeeId='" + assignEmployeeId + '\'' +
                ", assignEmployeeName='" + assignEmployeeName + '\'' +
                ", assignEmployeeMobile='" + assignEmployeeMobile + '\'' +
                ", bindCompanyName='" + bindCompanyName + '\'' +
                ", bindCompanyTaxid='" + bindCompanyTaxid + '\'' +
                ", bindPerson='" + bindPerson + '\'' +
                ", bindTime=" + bindTime +
                ", originCode='" + originCode + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
