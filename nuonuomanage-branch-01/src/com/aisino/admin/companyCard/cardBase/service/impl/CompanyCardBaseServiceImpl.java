package com.aisino.admin.companyCard.cardBase.service.impl;

import com.aisino.admin.companyCard.cardBase.dao.CompanyCardBaseDao;
import com.aisino.admin.companyCard.cardBase.service.CompanyCardBaseService;
import com.aisino.global.context.common.ex.ServiceException;
import com.aisino.global.context.common.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("companyCardBaseService")
public class CompanyCardBaseServiceImpl implements CompanyCardBaseService {
    @Autowired
    CompanyCardBaseDao companyCardBaseDao;

    /**
     * @Method : getCompany
     * @Description : 前端下拉列表所需的CompanyId
     * @param user :
     * @param c_companyid :
     * @return : java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:50:58
     */
    @Override
    public List<Map<String, Object>> getCompany(UserModel user, String c_companyid)
            throws ServiceException {
        return companyCardBaseDao.getCompany(user, c_companyid);
    }

    /**
     * @Method : getCompanyId
     * @Description : 获取CompanyId List，拼装成字符串
     * @param user :
     * @return : java.lang.String
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:49:34
     */
    @Override
    public String getCompanyId(UserModel user) throws ServiceException {
        List<String> list = companyCardBaseDao.getCompanyId(user);
        StringBuffer buffer = new StringBuffer();
        String ids = "";
        if (list != null) {
            for (String s : list) {
                buffer.append("\'" + s.trim() + "\'" + ",");
            }
            if (buffer.length() > 0) {
                ids = buffer.substring(0, buffer.length() - 1);
            }
        }
        return ids;
    }

    /**
     *根据companyId获取serviceIds
     * @param userModel  框架封装好的用户模型，包含一些用户信息
     * @param companyIds 前端下拉列表选择的companyIds
     * @return
     * @throws ServiceException
     */
    @Override
    public String getServicedId(UserModel userModel, String companyIds) throws ServiceException {
        String authCompanyIds = getCompanyId(userModel);
        String mergeCompanyIds = "";
        if (userModel.getUserType().toString().contains("SUPER")) {
            mergeCompanyIds = companyIds;
        }else {
            mergeCompanyIds = mergeIds(companyIds, authCompanyIds);
        }
        String serviceIds = "";
        List<String> list = companyCardBaseDao.getServiceId(mergeCompanyIds);
        if (list != null) {
            StringBuffer buffer = new StringBuffer();
            for (String s : list) {
                buffer.append("\'" + s.trim() + "\'" + ",");
            }
            if (buffer.length() > 0) {
                serviceIds = buffer.substring(0, buffer.length() - 1);
            }
        }
        return serviceIds;
    }

    /**
     * @Method : getServicedId
     * @Description : 根据companyId获取serviceIds
     * @param userModel :
     * @return : java.lang.String
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:48:08
     */
    @Override
    public String getServicedId(UserModel userModel) throws ServiceException {
        String authCompanyIds = getCompanyId(userModel);
        String serviceIds = "";
        List<String> list = companyCardBaseDao.getServiceId(authCompanyIds);
        StringBuffer buffer = new StringBuffer();
        if (list != null) {
            for (String s : list) {
                buffer.append("\'" + s.trim() + "\'" + ",");
            }
            if (buffer.length() > 0) {
                serviceIds = buffer.substring(0, buffer.length() - 1);
            }
        }
        return serviceIds;
    }

    /**
     * @Method : getServiceIdMap
     * @Description : 将serviceIds以Map方式返回
     * @param userModel :
     * @return : java.util.Map<java.lang.String,java.lang.Object>
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:47:41
     */
    @Override
    public Map<String, Object> getServiceIdMap(UserModel userModel) throws ServiceException {
        Map<String, Object> map = new HashMap<String, Object>();
        String serviceIds = getServicedId(userModel);
        if (serviceIds.trim().length() == 0) {
            serviceIds = null;
        }
        map.put("userType", userModel.getUserType());
        map.put("serviceIds", serviceIds);
        return map;
    }

    /**
     * @Method : getServiceIdMap
     * @Description : 将serviceIds以Map方式返回
     * @param userModel :
     * @param companyIds :
     * @return : java.util.Map<java.lang.String,java.lang.Object>
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:47:12
     */
    @Override
    public Map<String, Object> getServiceIdMap(UserModel userModel, String companyIds) throws ServiceException {
        Map<String, Object> map = new HashMap<String, Object>();
        String serviceIds = getServicedId(userModel, companyIds);
        if (serviceIds.trim().length() == 0) {
            serviceIds = null;
        }
        map.put("userType", userModel.getUserType());
        map.put("serviceIds", serviceIds);
        return map;
    }

    /**
     * @Method : mergeIds
     * @Description : 该函数用与取前端下拉列表选择的companyIds和用户权限内的companyIds的交集
     * @param selectIds : 前端下拉列表选择companyIds
     * @param authIds : 用户权限内的companyIds
     * @return : java.lang.String
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:46:19
     */
    public static String mergeIds(String selectIds, String authIds) {
        StringBuffer buffer = new StringBuffer();
        String ids = "";
        String[] authIdsArray = authIds.split(",");
        if (selectIds.trim().length() == 0 || selectIds == null) {
            ids = authIds;
        } else {
            for (String s : authIdsArray) {
                if (selectIds.contains(s)) {
                    buffer.append(s.trim() + ",");//此处不要加单引号，否则一个id将有两个单引号
                }
            }
            if (buffer.length() > 0) {
                ids = buffer.substring(0, buffer.length() - 1);
            }
        }
        return ids;
    }

}
