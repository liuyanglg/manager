package com.aisino.admin.companyCard.codeAssign.action;

import com.aisino.admin.companyCard.codeAssign.bean.KPAssignCodeVO;
import com.aisino.admin.companyCard.codeAssign.bean.KPCodeDetailDO;
import com.aisino.admin.companyCard.codeAssign.bean.KPCodeMainDO;
import com.aisino.admin.companyCard.codeAssign.service.CodeAssignService;
import com.aisino.admin.companyCard.codeAssign.service.CodeDetailService;
import com.aisino.admin.companyCard.codeAssign.service.CodeMainService;
import com.aisino.global.context.common.action.TemplateAction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class CodeAssignBaseAction extends TemplateAction {
    @Autowired
    protected CodeAssignService codeAssignService;

    @Autowired
    protected CodeMainService codeMainService;

    @Autowired
    protected CodeDetailService codeDetailService;

    protected Integer detailId;
    protected Integer mainId;
    protected String preAssignCode;
    protected String bindCompanyName;
    protected String bindCompanyTaxid;
    protected String bindPerson;
    protected Date bindTime;
    protected String originCode;
    protected String status;

    protected String assignCompanyName;
    protected String assignCompanyTaxid;
    protected String assignEmployeeId;
    protected String assignEmployeeName;
    protected String assignEmployeeMobile;
    protected Integer assignAmount;
    protected String createPerson;
    protected Date createTime;
    protected String modifyPerson;
    protected Date modifyTime;

    protected int rows;
    protected int page;
    protected String orderColumnName = "assign_company_name,assign_employee_name";

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

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public KPAssignCodeVO buildKPAssignCodeVO() {
        KPAssignCodeVO kpAssignCodeVO = new KPAssignCodeVO();
        kpAssignCodeVO.setDetailId(detailId);
        kpAssignCodeVO.setMainId(mainId);
        kpAssignCodeVO.setPreAssignCode(preAssignCode);
        kpAssignCodeVO.setBindCompanyName(bindCompanyName);
        kpAssignCodeVO.setBindCompanyTaxid(bindCompanyTaxid);
        kpAssignCodeVO.setBindPerson(bindPerson);
        kpAssignCodeVO.setBindTime(bindTime);
        kpAssignCodeVO.setOriginCode(originCode);
        kpAssignCodeVO.setStatus(status);
        kpAssignCodeVO.setAssignCompanyName(assignCompanyName);
        kpAssignCodeVO.setAssignCompanyTaxid(assignCompanyTaxid);
        kpAssignCodeVO.setAssignEmployeeId(assignEmployeeId);
        kpAssignCodeVO.setAssignEmployeeName(assignEmployeeName);
        kpAssignCodeVO.setAssignEmployeeMobile(assignEmployeeMobile);
        kpAssignCodeVO.setAssignAmount(assignAmount);
        kpAssignCodeVO.setCreatePerson(createPerson);
        kpAssignCodeVO.setCreateTime(createTime);
        kpAssignCodeVO.setModifyPerson(modifyPerson);
        kpAssignCodeVO.setModifyTime(modifyTime);
        return kpAssignCodeVO;
    }


    public KPCodeMainDO buildKPCodeMainDO() {
        KPCodeMainDO kpCodeMainDO = new KPCodeMainDO();
        kpCodeMainDO.setId(mainId);
        kpCodeMainDO.setAssignCompanyName(assignCompanyName);
        kpCodeMainDO.setAssignCompanyTaxid(assignCompanyTaxid);
        kpCodeMainDO.setAssignAmount(assignAmount);
        kpCodeMainDO.setCreatePerson(createPerson);
        kpCodeMainDO.setCreateTime(createTime);
        kpCodeMainDO.setModifyPerson(modifyPerson);
        kpCodeMainDO.setModifyTime(modifyTime);
        return kpCodeMainDO;
    }

    public KPCodeDetailDO buildKPCodeDetailDO() {
        KPCodeDetailDO kpCodeDetailDO = new KPCodeDetailDO();
        kpCodeDetailDO.setDetailId(detailId);
        kpCodeDetailDO.setMainId(mainId);
        kpCodeDetailDO.setPreAssignCode(preAssignCode);
        kpCodeDetailDO.setAssignEmployeeId(assignEmployeeId);
        kpCodeDetailDO.setAssignEmployeeName(assignEmployeeName);
        kpCodeDetailDO.setAssignEmployeeMobile(assignEmployeeMobile);
        kpCodeDetailDO.setBindCompanyName(bindCompanyName);
        kpCodeDetailDO.setBindCompanyTaxid(bindCompanyTaxid);
        kpCodeDetailDO.setBindPerson(bindPerson);
        kpCodeDetailDO.setBindTime(bindTime);
        kpCodeDetailDO.setOriginCode(originCode);
        kpCodeDetailDO.setStatus(status);
        return kpCodeDetailDO;
    }


}
