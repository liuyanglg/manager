package com.aisino.admin.companyCard.cardBase.service.impl;

import com.aisino.admin.companyCard.cardBase.dao.CompanyCardBaseDao;
import com.aisino.admin.companyCard.cardBase.service.CompanyCardBaseService;
import com.aisino.global.context.common.ex.ServiceException;
import com.aisino.global.context.common.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * @param companyId :
     * @return : java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:50:58
     */
    @Override
    public List<Map<String, Object>> getCompany(UserModel user, String companyId)
            throws ServiceException {
        return companyCardBaseDao.getCompany(user, companyId);
    }

    public String getServiceId(UserModel userModel, List<String> selectCmpIdList) throws ServiceException {
        List<String> mergeCmpIdList = new ArrayList<String>();

        List<String> authCmpIdList = companyCardBaseDao.getCmpIdByAuthority(userModel);
        if (selectCmpIdList == null || selectCmpIdList.size() <= 0) {
            mergeCmpIdList.addAll(authCmpIdList);
        } else if (authCmpIdList != null && authCmpIdList.size() > 0) {
            for (String id : authCmpIdList) {
                if (selectCmpIdList.contains(id)) {
                    mergeCmpIdList.add(id);
                }
            }
        }

        StringBuffer serviceIBuffer = new StringBuffer();
        String serviceIdString = null;
        List<String> serviceIdList = companyCardBaseDao.queryServiceId(mergeCmpIdList);
        if (serviceIdList != null && serviceIdList.size() > 0) {
            for (String id : serviceIdList) {
                serviceIBuffer.append("'" + id + "',");
            }
        }
        if (serviceIBuffer.length() > 0) {
            serviceIBuffer = serviceIBuffer.deleteCharAt(serviceIBuffer.length() - 1);
            serviceIdString = serviceIBuffer.toString();
        }

        return serviceIdString;
    }

}
