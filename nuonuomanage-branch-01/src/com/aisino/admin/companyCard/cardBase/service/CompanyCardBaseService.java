package com.aisino.admin.companyCard.cardBase.service;

import com.aisino.global.context.common.ex.ServiceException;
import com.aisino.global.context.common.model.UserModel;

import java.util.List;
import java.util.Map;

/**
 * @Project :
 * @Package : com.aisino.admin.companyCard.cardBase.service
 * @Class : CompanyCardBaseService
 * @Description : 为了获取服务单位Id创建的接口类
 * @author : liuya
 * @createDate : 2017-08-07 星期一 14:44:44
 * @version : V1.0.0
 * @Copyright : 2017 liuya Inc. All rights reserved.
 * @Reviewed :
 * @UpateLog :    Name    Date    Reason/Contents
 */
public interface CompanyCardBaseService {
    List<Map<String, Object>> getCompany(UserModel user, String companyId) throws ServiceException;

    String getServiceId(UserModel userModel, List<String> selectCmpIdList) throws ServiceException;
}
