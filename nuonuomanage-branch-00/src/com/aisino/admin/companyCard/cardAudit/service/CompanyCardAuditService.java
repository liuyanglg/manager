package com.aisino.admin.companyCard.cardAudit.service;

import com.aisino.admin.companyCard.cardAudit.bean.CompanyCardAudit;
import com.aisino.admin.global.paging.Page;
import com.aisino.global.context.common.model.UserModel;
import org.apache.http.ParseException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface CompanyCardAuditService {

    public CompanyCardAudit get(Integer id);

    public void update(CompanyCardAudit cardAudit);

    public void insert(CompanyCardAudit cardAudit);

    /**
     * 审核通过
     *
     * @param ids
     * @param user
     */
    public boolean approve(int[] ids, String user) throws ParseException, IOException;

    /**
     * 审核不通过
     *
     * @param ids
     * @param user
     */
    public boolean notApprove(int[] ids, String user);

    public boolean merge(int auditId, String code, String user, int type) throws ParseException, IOException;

    public List<CompanyCardAudit> queryPage(Page<CompanyCardAudit> page);

    public List<CompanyCardAudit> queryPage(Page<CompanyCardAudit> page, UserModel user, String companyId);

    public int count(Page<CompanyCardAudit> page);

    public int count(Page<CompanyCardAudit> page, UserModel user, String companyId);

    public List<CompanyCardAudit> queryPage4Metching(Page<CompanyCardAudit> page);

    public List<CompanyCardAudit> queryPage4Metching(Page<CompanyCardAudit> page, UserModel user, String companyId);

    public int count4Metching(Page<CompanyCardAudit> page);

    public int count4Metching(Page<CompanyCardAudit> page, UserModel user, String companyId);

    public void changeCert(CompanyCardAudit cardAudit);

    public void exportList(Page<CompanyCardAudit> page, HttpServletResponse response, UserModel user, String companyId);

    public void exportMatch(Page<CompanyCardAudit> page, HttpServletResponse response, UserModel user, String companyId);

}
