package com.aisino.admin.companyCard.cardMaintain.action;

import com.aisino.admin.companyCard.cardMaintain.bean.CmpCardApiResponse;
import com.aisino.admin.companyCard.cardMaintain.bean.CompanyCard;
import com.aisino.admin.companyCard.cardMaintain.utils.CmpCardApiUtils;
import com.aisino.admin.global.paging.Page;
import com.aisino.admin.global.utils.SqlInjectionProtection;
import com.aisino.admin.global.utils.StringUtils;
import com.aisino.global.context.common.utils.JsonUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
public class CompanyCardMaintainAction extends CompanyCardMaintainBaseAction {
    private CompanyCard companyCard;

    /**
     * @return : void
     * @Method : queryCardMaintain
     * @Description : 查询维护表记录
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:33:03
     */
    public void queryCardMaintainAction() {
        companyCard = buildCompanyCard();
        if (!queryLimit()) {
            this.sendMessage("查询频率过高");
            return;
        }

        CompanyCard companyCard = buildCompanyCard();
        map = new HashMap<String, Object>();
        int total = 0;
        List<CompanyCard> list = null;
        if (init == 0 && rows <= 100) {
            if (companyCard != null) {
                if (StringUtils.isBlank(companyCard.getCode())) {
                    companyCard.setCode(null);
                }
                if (StringUtils.isBlank(companyCard.getName())) {
                    companyCard.setName(null);
                }
                if (StringUtils.isBlank(companyCard.getTaxid())) {
                    companyCard.setTaxid(null);
                }
            } else {
                companyCard = new CompanyCard();
            }
            if (SqlInjectionProtection.sqlValidate(companyCard.getName())) {
                list = new ArrayList<CompanyCard>();
            } else if (SqlInjectionProtection.sqlValidate(companyCard.getTaxid())) {
                list = new ArrayList<CompanyCard>();
            } else {
                Page<CompanyCard> cardPage = new Page<CompanyCard>(companyCard);
                cardPage.setCurPageIndex(page);
                cardPage.setPageSize(rows);
                Map<String, Object> paramsMap = new HashMap<String, Object>();
                String userType = this.getUserModel().getUserType().toString();//获取用户类型
                paramsMap.put("userType", userType);
                if (userType.contains("SUPER")) {
                    /*超级用户查询所有数据*/
                    cardPage.setOtherParams(null);//设置为空值，MyBatis就不加限定条件了
                } else if (userType.contains("USER")) {
                    /*普通用户查询所有数据,但不能模糊查询*/
                    paramsMap.put("userType", userType);
                    cardPage.setOtherParams(paramsMap);
                }
                list = companyCardMaintainService.queryPage(cardPage);
                total = companyCardMaintainService.count(cardPage);
            }
        } else {
            list = new ArrayList<CompanyCard>();
        }
        map.put("total", total);
        map.put("rows", list);
        this.sendJson(JsonUtil.toDateFormatFastJsonString(map));
    }

    /**
     * @return : void
     * @Method : addCardMaintain
     * @Description : 添加一条记录
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:33:41
     */
    public void addCardMaintainAction() {
        companyCard = buildCompanyCard();
        String execResult = "";
        try {
            CmpCardApiResponse apiResponse = CmpCardApiUtils.post(companyCard.convertJson());
            if (apiResponse != null && "201".equals(apiResponse.getCode())) {
                execResult = "success";
            } else {
                execResult = "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            execResult = "error";
        } finally {
            this.sendMessage(execResult);
        }
    }

    /**
     * @return : java.lang.String
     * @Method : bmodCardMaintain
     * @Description : 调用API接口修改一条记录
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:34:45
     */
    public String bmodCardMaintainAction() {
        try {
            CmpCardApiResponse apiResponse = CmpCardApiUtils.get(code);
            if (apiResponse != null && "200".equals(apiResponse.getCode())) {
                CompanyCard card = apiResponse.getData();
                if (card.getName().indexOf("\"") >= 0) {
                    card.setName(card.getName().replaceAll("\"", "&quot;"));
                }
                getServletRequest().setAttribute("card", card);
                return "update";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String errorMessage = "{\"message\":\"error\"}";
        this.sendJson(errorMessage);
        return "update";
    }

    public void updateCardMaintainAction() {
        companyCard = buildCompanyCard();
        String execResult = "";
        try {
            companyCardMaintainService.update(companyCard);
            execResult = "success";
        } catch (Exception e) {
            execResult = "error";
            e.printStackTrace();
        } finally {
            this.sendMessage(execResult);
        }
    }

    /**
     * @return : java.lang.String
     * @Method : deleteCardMaintain
     * @Description : 删除一条记录
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:35:44
     */
    public void deleteCardMaintainAction() {
        String execResult = "";
        String user = this.getUserModel().getRealname();
        String[] batchCodes = new String[codes.size()];
        for (int i = 0; i < codes.size(); i++) {
            batchCodes[i] = codes.get(i);
        }
        boolean deleted = companyCardMaintainService.delete(batchCodes, user);
        if (deleted) {
            execResult = "success";
        } else {
            execResult = "error";
        }
        this.sendMessage(execResult);
    }

    /**
     * @return : void
     * @Method : shieldCardMaintain
     * @Description : 屏蔽记录
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:36:13
     */
    public void shieldCardMaintainAction() {
        String execResult = "";
        boolean s = true;
        String[] batchCodes = new String[codes.size()];
        for (int i = 0; i < codes.size(); i++) {
            batchCodes[i] = codes.get(i);
        }
        if (batchCodes != null && batchCodes.length > 0) {
            for (String code : batchCodes) {
                try {
                    CompanyCard card = new CompanyCard();
                    card.setStatus(8);
                    //通过接口更新数据库
                    CmpCardApiResponse apiResponse = CmpCardApiUtils.put(code, card.convertJson());
                    if (apiResponse != null && "201".equals(apiResponse.getCode())) {
                    } else {
                        s = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    s = false;
                } finally {
                    if (s) {
                        execResult = "success";
                    } else {
                        execResult = "error";
                    }
                }
            }
        }
        this.sendMessage(execResult);
    }

    /**
     * @Method : checkCardAction
     * @Description : 调用数据层接口检测六位代码是否合法
     * @ReturnType : void
     * @Author : lyf
     * @CreateDate : 2017-09-28 星期四 17:47:24
     */
    public void checkCardAction() {
        companyCard = buildCompanyCard();
        Boolean isCodeLegal = false;
        String message = "输入六位代码不合法";
        Map<String, Object> map = new HashMap<String, Object>();

        CmpCardApiResponse apiResponse = null;
        if (companyCard.getCode() != null && companyCard.getCode().trim().length() == 6) {
            try {
                apiResponse = CmpCardApiUtils.check(oldCode, companyCard.convertJson());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (apiResponse != null) {
            if (apiResponse.getCode().equals("200")) {
                isCodeLegal = true;
            }
            message = apiResponse.getMessage();
        } else {
            message = "查询出错！";
        }

        map.put("code", isCodeLegal);
        map.put("message", message);
        this.sendJson(JsonUtil.toDateFormatFastJsonString(map));
    }

    public String hrefEditCodeAction() {
        getServletRequest().setAttribute("code", oldCode);
        return "editCode";
    }

    /**
     * @Method : editCodeAction
     * @Description : 调用数据层接口修改六位代码
     * @ReturnType : void
     * @Author : lyf
     * @CreateDate : 2017-09-28 星期四 17:48:12
     */
    public void editCodeAction() {
        companyCard = buildCompanyCard();
        CmpCardApiResponse apiResponse = null;
        String execResult = "error";
        if (companyCard.getCode() != null && companyCard.getCode().trim().length() == 6) {
            try {
                apiResponse = CmpCardApiUtils.modify(oldCode, companyCard.convertJson());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (apiResponse != null) {
            if (apiResponse.getCode().equals("200"))
                execResult = "success";
        }
        this.sendMessage(execResult);
    }

    public boolean queryLimit() {
        boolean enableQuery = false;
        String userType = this.getUserModel().getUserType().toString();//获取用户类型
        if (userType.contains("SUPER")) {
            enableQuery = true;
        } else if (userType.contains("USER") && init == 0) {
            String key = "lastQueryTime";
            HttpSession session = this.getHttpSession();
            Long currentQueryTime = System.currentTimeMillis();
            Long lastQueryTime = (Long) session.getAttribute(key);
            if (lastQueryTime == null) {
                session.setAttribute(key, currentQueryTime);
            } else {
                Long timeDiff = currentQueryTime - lastQueryTime;
                if (timeDiff < 10 * 1000) {
                    enableQuery = false;
                } else {
                    enableQuery = true;
                    session.setAttribute(key, currentQueryTime);
                }
            }
        }
        return enableQuery;
    }
}