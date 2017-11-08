package com.aisino.admin.companyCard.cardMaintain.action;

import com.aisino.admin.companyCard.cardBase.service.CompanyCardBaseService;
import com.aisino.admin.companyCard.cardMaintain.bean.CompanyCard;
import com.aisino.admin.companyCard.cardMaintain.service.CompanyCardMaintainService;
import com.aisino.admin.global.utils.StringUtils;
import com.aisino.global.context.common.action.TemplateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Map;

@Controller
@Scope("prototype")
public class CompanyCardMaintainBaseAction extends TemplateAction {
    @Autowired
    @Qualifier("companyCardMaintainService")
    protected CompanyCardMaintainService companyCardMaintainService;
    @Autowired
    @Qualifier("companyCardBaseService")
    protected CompanyCardBaseService companyCardBaseService;
    protected Map<String, Object> map;
    protected String code;
    protected String taxid;
    protected String name;
    protected String address;
    protected String telephone;
    protected String bank;
    protected String account;
    protected Integer type;
    protected Integer cert;
    protected Integer source;
    protected Integer status;

    protected int init;
    protected String oldCode;
    protected ArrayList<String> codes;

    protected int rows;
    protected int page;

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public void setInit(int init) {
        this.init = init;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setCodes(ArrayList<String> codes) {
        this.codes = codes;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
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

    public void setType(Integer type) {
        this.type = type;
    }

    public void setCert(Integer cert) {
        this.cert = cert;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @Method : buildCompanyCard
     * @Description : 将页面传来的参数封装成CompanyCard对象
     * @return : com.aisino.admin.companyCard.cardMaintain.bean.CompanyCard
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:32:38
     */
    public CompanyCard buildCompanyCard() {
        CompanyCard companyCard = new CompanyCard();
        companyCard.setCode(convertStr(code));
        companyCard.setTaxid(convertStr(taxid));
        companyCard.setName(convertStr(name));
        companyCard.setAddress(convertStr(address));
        companyCard.setTelephone(convertStr(telephone));
        companyCard.setBank(convertStr(bank));
        companyCard.setAccount(convertStr(account));
        companyCard.setType(type);
        companyCard.setCert(cert);
        companyCard.setSource(source);
        companyCard.setStatus(status);

        return companyCard;
    }
    public String convertStr(String str) {
        if (StringUtils.isBlank(str)) {
            str = null;
        }
        return str;
    }
}