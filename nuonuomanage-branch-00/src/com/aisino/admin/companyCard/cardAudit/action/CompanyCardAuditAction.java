package com.aisino.admin.companyCard.cardAudit.action;

import com.aisino.admin.companyCard.cardAudit.bean.CompanyCardAudit;
import com.aisino.admin.companyCard.cardMaintain.bean.CmpCardApiResponse;
import com.aisino.admin.companyCard.cardMaintain.bean.CompanyCard;
import com.aisino.admin.companyCard.cardMaintain.utils.CmpCardApiUtils;
import com.aisino.admin.global.paging.Page;
import com.aisino.admin.global.utils.DateUtils;
import com.aisino.admin.global.utils.StringUtils;
import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

public class CompanyCardAuditAction extends CompanyCardAuditBaseAction {
    private static final int MAX_EXPORT_SIZE = 1000000;
    private CompanyCardAudit cardAudit;

    public void initCardAudit() {
        if (cardAudit != null && !StringUtils.isBlank(cardAudit.getDateRangStart())) {
            cardAudit.setDateRangStart(cardAudit.getDateRangStart().trim() + " 00:00:00");
        } else {
            cardAudit.setDateRangStart(DateUtils.dateBeforeOrAfter("yyyy-MM-dd", -7) + " 00:00:00");
        }
        if (cardAudit != null && !StringUtils.isBlank(cardAudit.getDateRangEnd())) {
            cardAudit.setDateRangEnd(cardAudit.getDateRangEnd().trim() + " 23:59:59");
        } else {
            cardAudit.setDateRangEnd(DateUtils.format(new Date(), "yyyy-MM-dd") + " 23:59:59");
        }
        if (StringUtils.isBlank(cardAudit.getName())) {
            cardAudit.setName(null);
        }
        if (StringUtils.isBlank(cardAudit.getTaxid())) {
            cardAudit.setTaxid(null);
        }
        if (StringUtils.isBlank(cardAudit.getCode())) {
            cardAudit.setCode(null);
        }
    }

    /**
     * @return : void
     * @Method : queryCardAudit
     * @Description : 查询CompanyCardAudit，以Json格式传给前端
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:26:28
     */
    public void queryCardAuditAction() {
        cardAudit = buildCardAudit();
        initCardAudit();
        List<CompanyCardAudit> list = null;
        int total = 0;
        if (rows <= 100) {
            Page<CompanyCardAudit> page = new Page<CompanyCardAudit>(cardAudit);
            page.setCurPageIndex(this.page);
            page.setPageSize(rows);
            page.setOrderCol("name");
            list = companyCardAuditService.queryPage(page, this.getUserModel(), this.companyId);
            total = companyCardAuditService.count(page, this.getUserModel(), this.companyId);
        } else {
            list = new ArrayList<CompanyCardAudit>();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", list);
        this.sendJson(JSON.toJSONString(map));
    }

    /**
     * @return : void
     * @Method : exportList
     * @Description : 查询符合条件的CompanyCardAudit，导出excel
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:27:20
     */
    public void exportListAction() {
        cardAudit = buildCardAudit();
        initCardAudit();
        List<CompanyCardAudit> list = null;
        Page<CompanyCardAudit> page = new Page<CompanyCardAudit>(cardAudit);
        page.setCurPageIndex(1);
        page.setPageSize(MAX_EXPORT_SIZE);
        companyCardAuditService.exportList(page, getServletResponse(), this.getUserModel(), this.companyId);
    }

    /**
     * @return : void
     * @Method : exportMatch
     * @Description : 根据公司名或税号模糊查询，然后导出excel
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:29:31
     */
    public void exportMatchAction() {
        cardAudit = buildCardAudit();
        List<CompanyCardAudit> list = null;
        if (!StringUtils.isBlank(nameOrTax)) {
            CompanyCardAudit audit = new CompanyCardAudit();
            audit.setName(nameOrTax);
            Page<CompanyCardAudit> page = new Page<CompanyCardAudit>(audit);
            page.setCurPageIndex(1);
            page.setPageSize(MAX_EXPORT_SIZE);
            companyCardAuditService.exportMatch(page, getServletResponse(), this.getUserModel(), this.companyId);
        }
    }


    /**
     * @return : void
     * @Method : matchNameOrTax
     * @Description : 根据公司名或税号模糊查询
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:29:04
     */
    public void matchNameOrTaxAction() {
        List<CompanyCardAudit> list = null;
        int total = 0;
        if (!StringUtils.isBlank(nameOrTax)) {
            CompanyCardAudit audit = new CompanyCardAudit();
            audit.setName(nameOrTax);
            Page<CompanyCardAudit> page = new Page<CompanyCardAudit>(audit);
            page.setCurPageIndex(this.page);
            list = companyCardAuditService.queryPage4Metching(page, this.getUserModel(), this.companyId);
            total = companyCardAuditService.count4Metching(page, this.getUserModel(), this.companyId);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", list);
        this.sendJson(JSON.toJSONString(map));
    }


    /**
     * @return : void
     * @Method : approveCardAudit
     * @Description : 通过审核
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:30:01
     */
    public void approveCardAuditAction() {
        String execResult = "";
        String user = super.getUserModel().getRealname();
        boolean bool = false;
        int[] batchIds = new int[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            batchIds[i] = ids.get(i);
        }
        try {
            bool = companyCardAuditService.approve(batchIds, user);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bool) {
                execResult = "success";
            } else {
                execResult = "error";
            }
        }
        this.sendMessage(execResult);
    }

    /**
     * @return : void
     * @Method : notApproveCardAudit
     * @Description : 不通过审核
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:30:35
     */
    public void notApproveCardAuditAction() {
        String execResult = "";
        String user = super.getUserModel().getRealname();
        boolean bool = false;
        int[] batchIds = new int[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            batchIds[i] = ids.get(i);
        }
        try {
            bool = companyCardAuditService.notApprove(batchIds, user);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bool) {
                execResult = "success";
            } else {
                execResult = "error";
            }
        }
        this.sendMessage(execResult);
    }

    /**
     * @return : void
     * @Method : editCardAudit
     * @Description : 编辑CompanyCardAudit信息
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:31:08
     */
    public void editCardAuditAction() {
        cardAudit = buildCardAudit();
        String execResult = "";
        execResult = "success";
        try {
            companyCardAuditService.update(cardAudit);
        } catch (Exception e) {
            execResult = "error";
        }
        this.sendMessage(execResult);
    }

    public void changeCertAction() {
        String execResult = "";
        execResult = "success";
        try {
            companyCardAuditService.changeCert(cardAudit);
        } catch (Exception e) {
            execResult = "error";
        }
        this.sendMessage(execResult);
    }

    public void mergeAction() {
        String execResult = "";
        String user = super.getUserModel().getRealname();
        boolean bool = false;
        try {
            bool = companyCardAuditService.merge(auditId, code, user, type);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bool) {
                execResult = "success";
            } else {
                execResult = "error";
            }
        }
        this.sendMessage(execResult);
    }


    public String compareCardAuditAction() {
        HttpServletRequest request = getServletRequest();
        CompanyCardAudit cardAudit = companyCardAuditService.get(id);
        String code = cardAudit.getCode();
        CmpCardApiResponse apiResponse = null;
        try {
            apiResponse = CmpCardApiUtils.get(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CompanyCard card = apiResponse.getData();
        if (null != card) {
            if (card.getSource() == null) {
                request.setAttribute("source_cp0", 1);
            } else {
                if (card.getSource().equals(cardAudit.getSource())) {
                    request.setAttribute("source_cp0", 0);
                } else {
                    request.setAttribute("source_cp0", 1);
                }
            }
            if (card.getName() == null) {
                request.setAttribute("name_cp0", 1);
            } else {
                if (card.getName().equals(cardAudit.getName())) {
                    request.setAttribute("name_cp0", 0);
                } else {
                    request.setAttribute("name_cp0", 1);
                }
            }
            if (card.getTaxid() == null) {
                request.setAttribute("taxid_cp0", 1);
            } else {
                if (card.getTaxid().equals(cardAudit.getTaxid())) {
                    request.setAttribute("taxid_cp0", 0);
                } else {
                    request.setAttribute("taxid_cp0", 1);
                }
            }
            if (card.getAddress() == null) {
                request.setAttribute("address_cp0", 1);
            } else {
                if (card.getAddress().equals(cardAudit.getAddress())) {
                    request.setAttribute("address_cp0", 0);
                } else {
                    request.setAttribute("address_cp0", 1);
                }
            }
            if (card.getTelephone() == null) {
                request.setAttribute("telephone_cp0", 1);
            } else {
                if (card.getTelephone().equals(cardAudit.getTelephone())) {
                    request.setAttribute("telephone_cp0", 0);
                } else {
                    request.setAttribute("telephone_cp0", 1);
                }
            }
            if (card.getBank() == null) {
                request.setAttribute("bank_cp0", 1);
            } else {
                if (card.getBank().equals(cardAudit.getBank())) {
                    request.setAttribute("bank_cp0", 0);
                } else {
                    request.setAttribute("bank_cp0", 1);
                }
            }
            if (card.getAccount() == null) {
                request.setAttribute("account_cp0", 1);
            } else {
                if (card.getAccount() != null && card.getAccount().equals(cardAudit.getAccount())) {
                    request.setAttribute("account_cp0", 0);
                } else {
                    request.setAttribute("account_cp0", 1);
                }
            }
            if (card.getCert() == null) {
                request.setAttribute("cert_cp0", 1);
            } else {
                if (card.getCert() != null && card.getCert().equals(cardAudit.getCert())) {
                    request.setAttribute("cert_cp0", 0);
                } else {
                    request.setAttribute("cert_cp0", 1);
                }
            }
            if (card.getType() == null) {
                request.setAttribute("type_cp0", 1);
            } else {
                if (card.getType() != null && card.getType().equals(cardAudit.getType())) {
                    request.setAttribute("type_cp0", 0);
                } else {
                    request.setAttribute("type_cp0", 1);
                }
            }
        }

        String name = cardAudit.getName();
        CompanyCard nameCard = null;
        if (!StringUtils.isBlank(name)) {
            nameCard = companyCardMaintainService.queryByName(name);
        }
        if (nameCard != null) {
            if (nameCard.getSource() == null) {
                request.setAttribute("source_cp1", 1);
            } else {
                if (nameCard.getSource().equals(cardAudit.getSource())) {
                    request.setAttribute("source_cp1", 0);
                } else {
                    request.setAttribute("source_cp1", 1);
                }
            }
            if (nameCard.getName() == null) {
                request.setAttribute("name_cp1", 1);
            } else {
                if (nameCard.getName().equals(cardAudit.getName())) {
                    request.setAttribute("name_cp1", 0);
                } else {
                    request.setAttribute("name_cp1", 1);
                }
            }
            if (nameCard.getTaxid() == null) {
                request.setAttribute("taxid_cp1", 1);
            } else {
                if (nameCard.getTaxid().equals(cardAudit.getTaxid())) {
                    request.setAttribute("taxid_cp1", 0);
                } else {
                    request.setAttribute("taxid_cp1", 1);
                }
            }
            if (nameCard.getAddress() == null) {
                request.setAttribute("address_cp1", 1);
            } else {
                if (nameCard.getAddress().equals(cardAudit.getAddress())) {
                    request.setAttribute("address_cp1", 0);
                } else {
                    request.setAttribute("address_cp1", 1);
                }
            }
            if (nameCard.getTelephone() == null) {
                request.setAttribute("telephone_cp1", 1);
            } else {
                if (nameCard.getTelephone().equals(cardAudit.getTelephone())) {
                    request.setAttribute("telephone_cp1", 0);
                } else {
                    request.setAttribute("telephone_cp1", 1);
                }
            }
            if (nameCard.getBank() == null) {
                request.setAttribute("bank_cp1", 1);
            } else {
                if (nameCard.getBank().equals(cardAudit.getBank())) {
                    request.setAttribute("bank_cp1", 0);
                } else {
                    request.setAttribute("bank_cp1", 1);
                }
            }
            if (nameCard.getAccount() == null) {
                request.setAttribute("account_cp1", 1);
            } else {
                if (nameCard.getAccount().equals(cardAudit.getAccount())) {
                    request.setAttribute("account_cp1", 0);
                } else {
                    request.setAttribute("account_cp1", 1);
                }
            }
            if (nameCard.getCert() == null) {
                request.setAttribute("cert_cp1", 1);
            } else {
                if (nameCard.getCert().equals(cardAudit.getCert())) {
                    request.setAttribute("cert_cp1", 0);
                } else {
                    request.setAttribute("cert_cp1", 1);
                }
            }
            if (nameCard.getType() == null) {
                request.setAttribute("type_cp1", 1);
            } else {
                if (nameCard.getType().equals(cardAudit.getType())) {
                    request.setAttribute("type_cp1", 0);
                } else {
                    request.setAttribute("type_cp1", 1);
                }
            }
        }
        request.setAttribute("cardAudit", cardAudit);
        if (nameCard != null && nameCard.getCode() != null) {
            if (!nameCard.getCode().equals(card.getCode())) {
                request.setAttribute("nameCard", nameCard);
            }
        }
        request.setAttribute("cmpCard", card);
        request.setAttribute("merge", 0);
        return "compare";
    }

    public String compareCardAuditNTAction() {
        HttpServletRequest request = getServletRequest();
        CompanyCardAudit cardAudit = companyCardAuditService.get(id);
        String name = cardAudit.getName();
        CompanyCard nameCard = null;
        if (!StringUtils.isBlank(name)) {
            nameCard = companyCardMaintainService.queryByName(name);
        }
        if (nameCard != null) {
            request.setAttribute("nameCard", nameCard);
            if (nameCard.getSource() == null) {
                request.setAttribute("source_cp1", 1);
            } else {
                if (nameCard.getSource().equals(cardAudit.getSource())) {
                    request.setAttribute("source_cp1", 0);
                } else {
                    request.setAttribute("source_cp1", 1);
                }
            }
            if (nameCard.getName() == null) {
                request.setAttribute("name_cp1", 1);
            } else {
                if (nameCard.getName().equals(cardAudit.getName())) {
                    request.setAttribute("name_cp1", 0);
                } else {
                    request.setAttribute("name_cp1", 1);
                }
            }
            if (nameCard.getTaxid() == null) {
                request.setAttribute("taxid_cp1", 1);
            } else {
                if (nameCard.getTaxid().equals(cardAudit.getTaxid())) {
                    request.setAttribute("taxid_cp1", 0);
                } else {
                    request.setAttribute("taxid_cp1", 1);
                }
            }
            if (nameCard.getAddress() == null) {
                request.setAttribute("address_cp1", 1);
            } else {
                if (nameCard.getAddress().equals(cardAudit.getAddress())) {
                    request.setAttribute("address_cp1", 0);
                } else {
                    request.setAttribute("address_cp1", 1);
                }
            }
            if (nameCard.getTelephone() == null) {
                request.setAttribute("telephone_cp1", 1);
            } else {
                if (nameCard.getTelephone().equals(cardAudit.getTelephone())) {
                    request.setAttribute("telephone_cp1", 0);
                } else {
                    request.setAttribute("telephone_cp1", 1);
                }
            }
            if (nameCard.getBank() == null) {
                request.setAttribute("bank_cp1", 1);
            } else {
                if (nameCard.getBank().equals(cardAudit.getBank())) {
                    request.setAttribute("bank_cp1", 0);
                } else {
                    request.setAttribute("bank_cp1", 1);
                }
            }
            if (nameCard.getAccount() == null) {
                request.setAttribute("account_cp1", 1);
            } else {
                if (nameCard.getAccount().equals(cardAudit.getAccount())) {
                    request.setAttribute("account_cp1", 0);
                } else {
                    request.setAttribute("account_cp1", 1);
                }
            }
            if (nameCard.getCert() == null) {
                request.setAttribute("cert_cp1", 1);
            } else {
                if (nameCard.getCert().equals(cardAudit.getCert())) {
                    request.setAttribute("cert_cp1", 0);
                } else {
                    request.setAttribute("cert_cp1", 1);
                }
            }
            if (nameCard.getType() == null) {
                request.setAttribute("type_cp1", 1);
            } else {
                if (nameCard.getType().equals(cardAudit.getType())) {
                    request.setAttribute("type_cp1", 0);
                } else {
                    request.setAttribute("type_cp1", 1);
                }
            }
        }

        String taxid = cardAudit.getTaxid();
        CompanyCard taxCard = null;
        if (!StringUtils.isBlank(taxid)) {
            taxCard = companyCardMaintainService.queryByTaxid(taxid);
        }
        if (taxCard != null) {
            if (nameCard == null || !nameCard.getCode().equals(taxCard.getCode())) {
                request.setAttribute("taxCard", taxCard);
                if (taxCard.getSource() == null) {
                    request.setAttribute("source_cp2", 1);
                } else {
                    if (taxCard.getSource().equals(cardAudit.getSource())) {
                        request.setAttribute("source_cp2", 0);
                    } else {
                        request.setAttribute("source_cp2", 1);
                    }
                }
                if (taxCard.getName() == null) {
                    request.setAttribute("name_cp2", 1);
                } else {
                    if (taxCard.getName().equals(cardAudit.getName())) {
                        request.setAttribute("name_cp2", 0);
                    } else {
                        request.setAttribute("name_cp2", 1);
                    }
                }
                if (taxCard.getTaxid() == null) {
                    request.setAttribute("taxid_cp2", 1);
                } else {
                    if (taxCard.getTaxid().equals(cardAudit.getTaxid())) {
                        request.setAttribute("taxid_cp2", 0);
                    } else {
                        request.setAttribute("taxid_cp2", 1);
                    }
                }
                if (taxCard.getAddress() == null) {
                    request.setAttribute("address_cp2", 1);
                } else {
                    if (taxCard.getAddress().equals(cardAudit.getAddress())) {
                        request.setAttribute("address_cp2", 0);
                    } else {
                        request.setAttribute("address_cp2", 1);
                    }
                }
                if (taxCard.getTelephone() == null) {
                    request.setAttribute("telephone_cp2", 1);
                } else {
                    if (taxCard.getTelephone().equals(cardAudit.getTelephone())) {
                        request.setAttribute("telephone_cp2", 0);
                    } else {
                        request.setAttribute("telephone_cp2", 1);
                    }
                }
                if (taxCard.getBank() == null) {
                    request.setAttribute("bank_cp2", 1);
                } else {
                    if (taxCard.getBank().equals(cardAudit.getBank())) {
                        request.setAttribute("bank_cp2", 0);
                    } else {
                        request.setAttribute("bank_cp2", 1);
                    }
                }
                if (taxCard.getAccount() == null) {
                    request.setAttribute("account_cp2", 1);
                } else {
                    if (taxCard.getAccount().equals(cardAudit.getAccount())) {
                        request.setAttribute("account_cp2", 0);
                    } else {
                        request.setAttribute("account_cp2", 1);
                    }
                }
                if (taxCard.getCert() == null) {
                    request.setAttribute("cert_cp2", 1);
                } else {
                    if (taxCard.getCert().equals(cardAudit.getCert())) {
                        request.setAttribute("cert_cp2", 0);
                    } else {
                        request.setAttribute("cert_cp2", 1);
                    }
                }
                if (taxCard.getType() == null) {
                    request.setAttribute("type_cp2", 1);
                } else {
                    if (taxCard.getType().equals(cardAudit.getType())) {
                        request.setAttribute("type_cp2", 0);
                    } else {
                        request.setAttribute("type_cp2", 1);
                    }
                }
            }
        }
        request.setAttribute("cardAudit", cardAudit);
        request.setAttribute("merge", 1);
        return "compare";
    }

    /**
     * @return : void
     * @Method : getCompanyTree
     * @Description : 返回用户拥有的公司Json数据，以树形图展示
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:31:59
     */
    public void getCompanyTreeAction() {
        List<Map<String, Object>> list = companyCardBaseService.getCompany(this.getUserModel(), companyId);
        this.sendMessage(JSON.toJSONString(list));
    }
}
