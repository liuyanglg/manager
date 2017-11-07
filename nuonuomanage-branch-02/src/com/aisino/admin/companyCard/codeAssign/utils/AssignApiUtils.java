package com.aisino.admin.companyCard.codeAssign.utils;

import com.aisino.global.context.common.utils.SpringUtils;
import com.aisino.projects.usercenter.redis.support.service.IUserCenterRedisService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjaisino.zhpt.remote.util.MD5Util;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class AssignApiUtils {

    public static List<Map<String, Object>> queryCompanyListApi(String queryCmpName, int size) throws ClientProtocolException, IOException {
        if (queryCmpName == null) {
            return null;
        }
        if (queryCmpName.trim().length() <= 0) {
            return null;
        }

        List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();

        Map globalConfig = (Map) SpringUtils.getBean("globalConfig");
        String url = (String) globalConfig.get("searchCenter.kpsearchUrl");
        url = url.replaceFirst("%s", queryCmpName.trim());
        if (size == 0) {
            size = 10;
        }
        url = url.replaceFirst("%d", size + "");

        CloseableHttpClient closeableHttpClient = null;
        try {
            closeableHttpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            JSONArray jsonArray = null;
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                jsonArray = JSON.parseArray(result);
            }
            if (jsonArray.size() > 0) {
                Iterator it = jsonArray.iterator();

                while (it.hasNext()) {
                    Map<String, Object> companyMap = new HashMap<String, Object>();
                    JSONObject jsonObject = (JSONObject) it.next();
                    String kpName = jsonObject.getString("name");
                    String kpTaxId = jsonObject.getString("tax_id");
                    companyMap.put("assignCompanyName", kpName);
                    companyMap.put("assignCompanyTaxid", kpTaxId);
                    companyList.add(companyMap);
                }
            }
        } finally {
            if (closeableHttpClient != null) {
                closeableHttpClient.close();
            }
        }

        return companyList;
    }

    public static Map<String, String> queryCompanyIdApi(String taxid) throws Exception, IOException {
        if (taxid == null) {
            return null;
        }
        if (taxid.trim().length() <= 0) {
            return null;
        }

        Map<String, String> resultMap = null;
        String resultJsonStr = "";

        Map globalConfig = (Map) SpringUtils.getBean("globalConfig");
        String url = "" + globalConfig.get("usercenter.url")
                + globalConfig.get("usercenter.syncuser.url");

        org.apache.commons.httpclient.HttpClient httpclient = null;
        PostMethod post = null;
        httpclient = new org.apache.commons.httpclient.HttpClient();
        httpclient.setConnectionTimeout(50000);
        httpclient.setTimeout(50000);
        post = new PostMethod(url);
        String random = String.valueOf(Math.random());
        String times = String.valueOf(System.currentTimeMillis());
        String USERCENTERAIP_KEY = (String) globalConfig.get("usercenter.syncuser.usercenteraip.key");
        String result = MD5Util.toMD5(USERCENTERAIP_KEY + times + random);
        post.addParameter((String) globalConfig.get("usercenter.syncuser.param"), taxid);
        post.addParameter("times", times);
        post.addParameter("random", random);
        post.addParameter("result", result);
        httpclient.executeMethod(post);
        resultJsonStr = new String(post.getResponseBody(), "utf-8");
        resultMap = (Map<String, String>) JSON.parse(resultJsonStr);

        return resultMap;
    }

    public static List<Map<String, Object>> queryEmployeeListApi(String companyId) {
        if (companyId == null) {
            return null;
        }
        if (companyId.trim().length() <= 0) {
            return null;
        }

        List<Map<String, Object>> employeeList = null;
        IUserCenterRedisService companyApi = (IUserCenterRedisService) SpringUtils.getBean("companyApi");
        employeeList = companyApi.getAllEmployeeList(companyId);
        return employeeList;
    }

}
