package com.aisino.admin.companyCard.cardAudit.bean;

import com.aisino.admin.global.utils.DateUtils;
import com.aisino.admin.global.utils.StringUtils;

import java.io.Serializable;
import java.util.Date;


public class CompanyCardAudit implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String code;			//六位代码
	private String taxid;			//税号
	private String name;			//企业名称
	private String address;			//地址
	private String telephone;		//电话
	private String bank;			//开户行
	private String account;			//银行账号
	private Integer source;			//数据来源
	private Integer type;			//纳税人标志
	private Date createTime;
	private String note;
	private Integer cert;			//认证标志
	private Integer status;			//审核状态:0未审核，1审核通过，-1审核不通过
	private String auditor;			//审核人
	private Date auditTime;			//审核时间
	
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusStr(){
		String str = null;
		if(status!=null){
			switch (status) {
			case 1:
				str = "审核通过";
				break;
			case -1:
				str = "审核不通过";
				break;
			default:
				str = "未审核";
				break;
			}
		}
		return str;
	}
	//查询参数字段
	private String dateRangStart;	//开始日期
	private String dateRangEnd;		//结束日期
	private Integer operType;		//操作类型   null:全部,0:新增(无六位代码),1:修改(有六位代码)
	
	public Integer getOperType() {
		return operType;
	}
	public void setOperType(Integer operType) {
		this.operType = operType;
	}
	public String getOperTypeStr(){
		String str = null;
		if(StringUtils.isBlank(code)){
			str = "新增";
		}
		else{
			str = "修改";
		}
		return str;
	}
	public String getDateRangStart() {
		return dateRangStart;
	}
	public void setDateRangStart(String dateRangStart) {
		this.dateRangStart = dateRangStart;
	}
	public String getDateRangEnd() {
		return dateRangEnd;
	}
	public void setDateRangEnd(String dateRangEnd) {
		this.dateRangEnd = dateRangEnd;
	}
	public Integer getCert() {
		return cert;
	}
	public void setCert(Integer cert) {
		this.cert = cert;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateTimeStr(){
		String str = null;
		if (createTime!=null) {
			str = DateUtils.format(createTime);
		}
		return str;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
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
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public String getSourceStr(){
		String str = null;
		if(source!=null){
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
			case 40:
				str = "用户中心";
				break;
			case 50:
				str = "请求开票";
				break;
			case 99:
				str = "ADMIN";
				break;
			}
		}
		else{
			str = "未知";
		}
		return str;
	}
	public String getCertStr(){
		String str = null;
		if(cert!=null){
			if(cert==1){
				str = "已认证";
			}
			else{
				str = "未认证";
			}
		}
		else{
			str = "未知";
		}
		return str;
	}
	public String getTypeStr(){
		String str = null;
		if(type!=null){
			if(type==0){
				str = "一般纳税人";
			}
			else if(type==1){
				str = "小规模";
			}
			else if(type==2){
				str = "个体工商户";
			}
		}
		return str;
	}
	
	public String convertJson(){
		StringBuilder builder = new StringBuilder();
		builder.append("{")
			.append(StringUtils.joinStringField4Json("taxid",this.getTaxid()))
			.append(StringUtils.joinStringField4Json("name",this.getName()))
			.append(StringUtils.joinStringField4Json("address",this.getAddress()))
			.append(StringUtils.joinStringField4Json("telephone",this.getTelephone()))
			.append(StringUtils.joinStringField4Json("bank",this.getBank()))
			.append(StringUtils.joinStringField4Json("account",this.getAccount()))
			.append(StringUtils.joinIntegerField4Json("type", this.getType()))
			.append(StringUtils.joinIntegerField4Json("cert", this.getCert()))
			.append(StringUtils.joinIntegerField4Json("source", this.getSource()));
		String json = (String)builder.subSequence(0, builder.length()-1);
		json += "}";
		return json;
	}
	
	public String getAuditTimeStr(){
		String str = null;
		if(auditTime!=null){
			str = DateUtils.format(auditTime);
		}
		return str;
	}
	
	public Integer compareSource(Integer osource){
		Integer compResult = null;
		if(this.source!=null){
			if(osource!=null){
				if(source==10||source==11){
					compResult = source;
				}
				else if(source==30||source==32||source==40){
					if(osource==10||osource==11){
						compResult = osource;
					}
					else{
						compResult = source;
					}
				}
				else if(source==12){
					if(osource==10||osource==11||osource==30||osource==32||osource==40){
						compResult = osource;
					}
					else{
						compResult = source;
					}
				}
				else if(source==50||source==31){
					if(osource==10||osource==11||osource==30||osource==32||osource==40||osource==12){
						compResult = osource;
					}
					else{
						compResult = source;
					}
				}
				else if(source==20||source==0){
					if(osource==20||osource==0){
						compResult = source;
					}
					else{
						compResult = osource;
					}
				}
			}
			else{
				compResult = source;
			}
		}
		else{
			compResult = osource;
		}
		return compResult;
	}
}
