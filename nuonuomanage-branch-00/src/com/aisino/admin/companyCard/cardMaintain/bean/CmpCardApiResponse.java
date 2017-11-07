package com.aisino.admin.companyCard.cardMaintain.bean;

import java.io.Serializable;

public class CmpCardApiResponse implements Serializable {

	private static final long serialVersionUID = 8682109128768414465L;

	private String code;
	private String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private CompanyCard data;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public CompanyCard getData() {
		return data;
	}
	public void setData(CompanyCard data) {
		this.data = data;
	}
	
}
