package com.aisino.admin.companyCard.cardAudit.service;

import com.aisino.admin.companyCard.cardAudit.bean.CompanyCardAudit;
import com.aisino.admin.global.paging.Page;
import org.apache.http.ParseException;

import java.io.IOException;
import java.util.List;

public interface CompanyCardAuditService {

	public CompanyCardAudit get(Integer id);
	
	public void update(CompanyCardAudit cardAudit);
	
	public void insert(CompanyCardAudit cardAudit);
	
	/**
	 * 审核通过
	 * @param ids
	 * @param user
	 */
	public boolean approve(int[] ids, String user) throws ParseException, IOException;
	
	/**
	 * 审核不通过
	 * @param ids
	 * @param user
	 */
	public boolean notApprove(int[] ids, String user);
	
	public boolean merge(int auditId, String code, String user, int type) throws ParseException, IOException;
	
	public List<CompanyCardAudit> queryPage(Page<CompanyCardAudit> page);
	
	public int count(Page<CompanyCardAudit> page);
	
	public List<CompanyCardAudit> queryPage4Metching(Page<CompanyCardAudit> page);
	
	public int count4Metching(Page<CompanyCardAudit> page);
	
	public void changeCert(CompanyCardAudit cardAudit);

}
