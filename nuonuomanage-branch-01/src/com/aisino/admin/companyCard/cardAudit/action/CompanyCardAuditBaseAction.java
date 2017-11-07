package com.aisino.admin.companyCard.cardAudit.action;

import com.aisino.admin.companyCard.cardAudit.bean.CompanyCardAudit;
import com.aisino.admin.companyCard.cardAudit.service.CompanyCardAuditService;
import com.aisino.admin.companyCard.cardBase.service.CompanyCardBaseService;
import com.aisino.admin.companyCard.cardMaintain.bean.CmpCardApiResponse;
import com.aisino.admin.companyCard.cardMaintain.bean.CompanyCard;
import com.aisino.admin.companyCard.cardMaintain.service.CompanyCardMaintainService;
import com.aisino.admin.companyCard.cardMaintain.utils.CmpCardApiUtils;
import com.aisino.admin.global.paging.Page;
import com.aisino.admin.global.utils.DateUtils;
import com.aisino.admin.global.utils.StringUtils;
import com.aisino.global.context.common.action.TemplateAction;
import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class CompanyCardAuditBaseAction extends TemplateAction {
    protected static final int MAX_EXPORT_SIZE = 1000000;
    @Autowired
    @Qualifier("companyCardAuditService")
    CompanyCardAuditService companyCardAuditService;
    @Autowired
    @Qualifier("companyCardMaintainService")
    protected CompanyCardMaintainService companyCardMaintainService;
    @Autowired
    @Qualifier("companyCardBaseService")
    protected CompanyCardBaseService companyCardBaseService;

    protected Integer id;
    protected String code;
    protected String taxid;
    protected String name;
    protected String address;
    protected String telephone;
    protected String bank;
    protected String account;
    protected Integer source;
    protected Integer type;
    protected Date createTime;
    protected String note;
    protected Integer cert;
    protected Integer status;
    protected String auditor;
    protected Date auditTime;
    protected String dateRangStart;
    protected String dateRangEnd;
    protected Integer operType;

    protected String nameOrTax;
    protected ArrayList<Integer> ids;
    protected int auditId;
    protected String companyId;//用户拥有权限公司Id，可能含多个

    protected int rows;
    protected int page;

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCert(Integer cert) {
        this.cert = cert;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public void setDateRangStart(String dateRangStart) {
        this.dateRangStart = dateRangStart;
    }

    public void setDateRangEnd(String dateRangEnd) {
        this.dateRangEnd = dateRangEnd;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public void setNameOrTax(String nameOrTax) {
        this.nameOrTax = nameOrTax;
    }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return : com.aisino.admin.companyCard.cardAudit.bean.CompanyCardAudit
     * @Method : buildCardAudit
     * @Description : 将页面传来的参数封装成CompanyCardAudit对象
     * @author : liuya
     * @CreateDate : 2017-08-07 星期一 13:28:12
     */
    public CompanyCardAudit buildCardAudit() {
        CompanyCardAudit cardAudit = new CompanyCardAudit();
        cardAudit.setId(id);
        cardAudit.setCode(code);
        cardAudit.setTaxid(taxid);
        cardAudit.setName(name);
        cardAudit.setAddress(address);
        cardAudit.setTelephone(telephone);
        cardAudit.setBank(bank);
        cardAudit.setAccount(account);
        cardAudit.setSource(source);
        cardAudit.setType(type);
        cardAudit.setCreateTime(createTime);
        cardAudit.setNote(note);
        cardAudit.setCert(cert);
        cardAudit.setStatus(status);
        cardAudit.setAuditor(auditor);
        cardAudit.setAuditTime(auditTime);
        cardAudit.setDateRangEnd(dateRangEnd);
        cardAudit.setDateRangStart(dateRangStart);
        cardAudit.setOperType(operType);
        return cardAudit;
    }

    public List<String> getCmpIdBySelect() {
        List<String> list = null;
        if (this.companyId == null || this.companyId.trim().length() == 0) {
            return list;
        }

        list = new ArrayList<String>();
        String[] ids = this.companyId.split(",");
        for (String id : ids) {
            if (id.trim().length() > 0) {
                list.add(id.trim());
            }
        }
        return list;
    }
}