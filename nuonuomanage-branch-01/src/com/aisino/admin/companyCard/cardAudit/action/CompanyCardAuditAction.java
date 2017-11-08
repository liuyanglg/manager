package com.aisino.admin.companyCard.cardAudit.action;

import com.aisino.admin.companyCard.cardAudit.bean.CompanyCardAudit;
import com.aisino.admin.companyCard.cardMaintain.bean.CmpCardApiResponse;
import com.aisino.admin.companyCard.cardMaintain.bean.CompanyCard;
import com.aisino.admin.companyCard.cardMaintain.utils.CmpCardApiUtils;
import com.aisino.admin.global.paging.Page;
import com.aisino.admin.global.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class CompanyCardAuditAction extends CompanyCardAuditBaseAction {
    /**
     * @return : void
     * @Method : queryCardAudit
     * @Description : 查询CompanyCardAudit，以Json格式传给前端
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:26:28
     */
    public void queryCardAuditAction() {
        CompanyCardAudit cardAudit = buildCardAudit();//将前端查询参数封装成对象
        List<CompanyCardAudit> list = null;
        int total = 0;
        if (rows <= 100) {
            Page<CompanyCardAudit> page = new Page<CompanyCardAudit>(cardAudit);
            page.setCurPageIndex(this.page);
            page.setPageSize(rows);
            page.setOrderCol("name");
            setAuthorityPage(page);
            list = companyCardAuditService.queryPage(page);
            total = companyCardAuditService.count(page);
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
        CompanyCardAudit cardAudit = buildCardAudit();
        List<CompanyCardAudit> list = null;
        Page<CompanyCardAudit> page = new Page<CompanyCardAudit>(cardAudit);
        page.setCurPageIndex(1);
        page.setPageSize(MAX_EXPORT_SIZE);
        setAuthorityPage(page);
        list = companyCardAuditService.queryPage(page);
        if (list == null || list.size() >= MAX_EXPORT_SIZE) {
            this.sendMessage("error");
        } else {
            exportDatas(list);
        }
    }

    /**
     * @param list :
     * @return : void
     * @Method : exportDatas
     * @Description : 导出Excel的实现方法
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:27:54
     */
    private void exportDatas(List<CompanyCardAudit> list) {
        HttpServletResponse response = getServletResponse();
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("开票信息审核列表");
        Row row = sheet.createRow((int) 0);
        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        Cell cell = null;
        String[] headers = {"操作类型", "数据来源", "审核状态", "六位代码", "企业名称", "税号", "地址", "电话", "开户行", "银行账号", "认证状态", "创建时间", "纳税人标识", "审核人", "审核时间"};
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                CompanyCardAudit audit = list.get(i);
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(StringUtils.isBlank(audit.getCode()) ? "新增" : "修改");
                row.createCell(1).setCellValue(audit.getSourceStr());
                row.createCell(2).setCellValue(audit.getStatusStr());
                row.createCell(3).setCellValue(audit.getCode());
                row.createCell(4).setCellValue(audit.getName());
                row.createCell(5).setCellValue(audit.getTaxid());
                row.createCell(6).setCellValue(audit.getAddress());
                row.createCell(7).setCellValue(audit.getTelephone());
                row.createCell(8).setCellValue(audit.getBank());
                row.createCell(9).setCellValue(audit.getAccount());
                row.createCell(10).setCellValue(audit.getCertStr());
                row.createCell(11).setCellValue(audit.getCreateTimeStr());
                row.createCell(12).setCellValue(audit.getTypeStr());
                row.createCell(13).setCellValue(audit.getAuditor());
                row.createCell(14).setCellValue(audit.getAuditTimeStr());
            }
        }
        //导出的文件名
        String fileName = "kpInfoAudit_" + (new SimpleDateFormat("yyyyMMddHH").format(new Date())) + ".xlsx";
        OutputStream os = null;
        try {
            os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
            response.setContentType("application/x-download");
            wb.write(os);
        } catch (IOException e) {
        } finally {
            if (null != wb) {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
            setAuthorityPage(page);
            list = companyCardAuditService.queryPage4Metching(page);
            total = companyCardAuditService.count4Metching(page);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", list);
        this.sendJson(JSON.toJSONString(map));
    }

    /**
     * @return : void
     * @Method : exportMatch
     * @Description : 根据公司名或税号模糊查询，然后导出excel
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:29:31
     */
    public void exportMatchAction() {
        List<CompanyCardAudit> list = null;
        if (!StringUtils.isBlank(nameOrTax)) {
            CompanyCardAudit audit = new CompanyCardAudit();
            audit.setName(nameOrTax);
            Page<CompanyCardAudit> page = new Page<CompanyCardAudit>(audit);
            page.setCurPageIndex(1);
            page.setPageSize(MAX_EXPORT_SIZE);
            setAuthorityPage(page);
            list = companyCardAuditService.queryPage4Metching(page);
        }
        if (list == null || list.size() >= MAX_EXPORT_SIZE) {
            this.sendMessage("error");
        } else {
            exportDatas(list);
        }
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
        CompanyCardAudit cardAudit = buildCardAudit();
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
        CompanyCardAudit cardAudit = buildCardAudit();
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
        CompanyCard card = null;
        try {
            apiResponse = CmpCardApiUtils.get(code);
            card = apiResponse.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        CompanyCard paramCard = new CompanyCard();//为了传递name和taxid
        paramCard.setName(name);
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
        CompanyCard paramCard = new CompanyCard();//为了传递name和taxid
        paramCard.setName(name);

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
        paramCard.setTaxid(taxid);
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

    /**
     * @method : setAuthorityPage
     * @description : 通过Page.otherParams设置查询权限
     * @param page :  分页查询参数
     * @return : void
     * @author : liuya
     * @date : 2017-11-08 星期三 11:11:32
     */
    public void setAuthorityPage(Page page) {
        String userType = this.getUserModel().getUserType().toString();//获取用户类型
        List<String> selectCmpIdList = super.getCmpIdBySelect();
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        String serviceIds = companyCardBaseService.getServiceId(this.getUserModel(), selectCmpIdList);//获取服务单位Id
        paramsMap.put("serviceIds", serviceIds);
        page.setOtherParams(paramsMap);
        if (userType.contains("SUPER")) {
            if (selectCmpIdList == null || selectCmpIdList.size() <= 0) {
                page.setOtherParams(null);//下拉列表不勾选，SUPER类用户查询所有服务单位数据
            }
        }
    }
}
