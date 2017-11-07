package com.aisino.admin.companyCard.cardAudit.dao;

import com.aisino.admin.companyCard.cardAudit.bean.CompanyCardAudit;
import com.aisino.admin.global.paging.Page;

import java.util.List;
import java.util.Map;


public interface CompanyCardAuditDao {

    public CompanyCardAudit get(Integer id);

    public void update(CompanyCardAudit cardAudit);

    public void insert(CompanyCardAudit cardAudit);

    public int delete(Integer id);

    public List<CompanyCardAudit> queryPage(Page<CompanyCardAudit> page);

    public List<CompanyCardAudit> queryByIds(int[] ids);

    public int deleteBatch(int[] ids);

    public int checkCode(String code);

    public int count(Page<CompanyCardAudit> page);

    public List<CompanyCardAudit> queryPage4Metching(Page<CompanyCardAudit> page);

    public int count4Metching(Page<CompanyCardAudit> page);

    public void approveList(Map<String, Object> map);

    public void notApproveList(Map<String, Object> map);

    public void changeCert(CompanyCardAudit cardAudit);
}
