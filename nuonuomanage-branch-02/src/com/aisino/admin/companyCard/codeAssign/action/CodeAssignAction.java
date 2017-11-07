package com.aisino.admin.companyCard.codeAssign.action;

import com.aisino.admin.companyCard.codeAssign.bean.KPAssignCodeVO;
import com.aisino.admin.companyCard.codeAssign.bean.KPCodeDetailDO;
import com.aisino.admin.companyCard.codeAssign.bean.KPCodeMainDO;
import com.aisino.admin.companyCard.codeAssign.utils.AssignApiUtils;
import com.aisino.admin.global.paging.Page;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.*;

public class CodeAssignAction extends CodeAssignBaseAction {
    private KPAssignCodeVO kpAssignCodeVO;

    /**
     * @method : queryCodeAssignAction
     * @description : 查询分配信息
     * @return : void
     * @author : liuya
     * @date : 2017-10-24 星期二 17:48:31
     */
    public void queryCodeAssignAction() {
        kpAssignCodeVO = this.buildKPAssignCodeVO();
        List<KPAssignCodeVO> list = null;
        int total = 0;
        if (rows <= 100) {
            Page<KPAssignCodeVO> page = new Page<KPAssignCodeVO>(kpAssignCodeVO);
            page.setCurPageIndex(this.page);
            page.setPageSize(rows);
            page.setOrderCol(orderColumnName);
            list = codeAssignService.queryPage(page);
            total = codeAssignService.count(page);
        } else {
            list = new ArrayList<KPAssignCodeVO>();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", list);
        this.sendJson(JSON.toJSONString(map));
    }

    public void fuzzyQueryCodeAssignAction() {
        kpAssignCodeVO = this.buildKPAssignCodeVO();
        System.out.println("kpAssignCodeVO: "+kpAssignCodeVO);
        List<KPAssignCodeVO> list = null;
        int total = 0;
        if (rows <= 100) {
            Page<KPAssignCodeVO> page = new Page<KPAssignCodeVO>(kpAssignCodeVO);
            page.setCurPageIndex(this.page);
            page.setPageSize(rows);
            page.setOrderCol(orderColumnName);
            list = codeAssignService.fuzzyQueryPage(page);
            total = codeAssignService.fuzzyCount(page);
        } else {
            list = new ArrayList<KPAssignCodeVO>();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", list);
        this.sendJson(JSON.toJSONString(map));
    }

    /**
     * @method : queryCompanyInfoAction
     * @description : 通过接口查询企业信息
     * @return : void
     * @author : liuya
     * @date : 2017-10-24 星期二 18:12:36
     */
    public void queryCompanyInfoAction() {
        List<Map<String, Object>> list = null;
        try {
            list = AssignApiUtils.queryCompanyListApi(assignCompanyName, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.sendJson(JSON.toJSONString(list));
    }

    /**
     * @method : queryEmployeeInfoAction
     * @description : 通过接口查询企业员工列表
     * @return : void
     * @author : liuya
     * @date : 2017-10-24 星期二 18:13:16
     */
    public void queryEmployeeInfoAction() {
        List<Map<String, Object>> list = null;
        list = codeAssignService.queryEmployeeList(assignCompanyTaxid);
        this.sendJson(JSON.toJSONString(list));
    }

    /**
     * @method : assignCodesAction
     * @description : 分配六位代码
     * @return : void
     * @author : liuya
     * @date : 2017-10-24 星期二 18:13:53
     */
    public void assignCodesAction() {
        String result = "";
        KPCodeMainDO kpCodeMainDO = buildKPCodeMainDO();
        KPCodeDetailDO kpCodeDetailDO = buildKPCodeDetailDO();
        kpCodeMainDO.setCreateTime(new Date());
        kpCodeMainDO.setCreatePerson(this.getUserModel().getRealname());
        boolean success = false;
        try {
            success = codeAssignService.assignCodes(kpCodeMainDO,kpCodeDetailDO);
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        if (success) {
            result = "success";
        } else {
            result = "error";
        }

        this.sendMessage(result);
    }
}
