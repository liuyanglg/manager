package com.aisino.admin.companyCard.cardMaintain.bean;

import com.aisino.admin.global.utils.StringUtils;

import java.io.Serializable;

public class CompanyCard implements Serializable {

    private static final long serialVersionUID = 4089983681687621938L;
    private Integer id;
    private String code;
    private String taxid;
    private String name;
    private String address;
    private String telephone;
    private String bank;
    private String account;
    private Integer type;
    private Integer cert;
    private Integer source;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTaxid() {
        return taxid;
    }

    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCert() {
        return cert;
    }

    public void setCert(Integer cert) {
        this.cert = cert;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSourceStr() {
        String str = null;
        if (source != null) {
            switch (source) {
                case 10:
                    str = "开票软件";
                    break;
                case 11:
                    str = "开票软件-百旺";
                    break;
                case 12:
                    str = "购方信息";
                    break;
                case 20:
                    str = "CRM";
                    break;
                case 30:
                    str = "诺诺网";
                    break;
                case 31:
                    str = "微信未登录";
                    break;
                case 32:
                    str = "微信已登录";
                    break;
                case 33:
                    str = "工商信息";
                    break;
                case 40:
                    str = "用户中心";
                    break;
                case 50:
                    str = "请求开票";
                    break;
                case 99:
                    str = "ADMIN";
                    break;
                default:
                    str = "未知";
                    break;
            }
        } else {
            str = "未知";
        }
        return str;
    }

    public String getCertStr() {
        String str = null;
        if (cert != null) {
            if (cert == 1) {
                str = "已认证";
            } else {
                str = "未认证";
            }
        } else {
            str = "未知";
        }
        return str;
    }

    public String getTypeStr() {
        String str = null;
        if (type != null) {
            if (type == 0) {
                str = "一般纳税人";
            } else if (type == 1) {
                str = "小规模";
            } else if (type == 2) {
                str = "个体工商户";
            }
        }
        return str;
    }

    public String getStatusStr() {
        String str = null;
        if (status != null) {
            switch (status) {
                case 0:
                    str = "常规显示";
                    break;
                case 8:
                    str = "已屏蔽";
                    break;
                case 9:
                    str = "已删除";
                    break;
                default:
                    str = "常规显示";
                    break;
            }
        }
        return str;
    }

    public String convertJson() {
        StringBuilder builder = new StringBuilder();
        builder.append("{")
                .append(StringUtils.joinStringField4Json("code", this.getCode()))
                .append(StringUtils.joinStringField4Json("taxid", this.getTaxid()))
                .append(StringUtils.joinStringField4Json("name", this.getName()))
                .append(StringUtils.joinStringField4Json("address", this.getAddress()))
                .append(StringUtils.joinStringField4Json("telephone", this.getTelephone()))
                .append(StringUtils.joinStringField4Json("bank", this.getBank()))
                .append(StringUtils.joinStringField4Json("account", this.getAccount()))
                .append(StringUtils.joinIntegerField4Json("type", this.getType()))
                .append(StringUtils.joinIntegerField4Json("cert", this.getCert()))
                .append(StringUtils.joinIntegerField4Json("source", this.getSource()))
                .append(StringUtils.joinIntegerField4Json("status", this.getStatus()));
        String json = (String) builder.subSequence(0, builder.length() - 1);
        json += "}";
        return json;
    }
}
