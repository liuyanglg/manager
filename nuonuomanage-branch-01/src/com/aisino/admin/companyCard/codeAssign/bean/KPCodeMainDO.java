package com.aisino.admin.companyCard.codeAssign.bean;

import java.util.Date;

public class KPCodeMainDO {
    private Integer id;
    private String assignCompanyName;
    private String assignCompanyTaxid;
    private Integer assignAmount;
    private String createPerson;
    private Date createTime;
    private String modifyPerson;
    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "KPCodeMainDO{" +
                "id=" + id +
                ", assignCompanyName='" + assignCompanyName + '\'' +
                ", assignCompanyTaxid='" + assignCompanyTaxid + '\'' +
                ", assignAmount=" + assignAmount +
                ", createPerson='" + createPerson + '\'' +
                ", createTime=" + createTime +
                ", modifyPerson='" + modifyPerson + '\'' +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
