package com.aisino.admin.companyCard.cardBase.dao;

import com.aisino.global.context.common.ex.DAOException;
import com.aisino.global.context.common.model.UserModel;

import java.util.List;
import java.util.Map;

/**
 * @Project :
 * @Package : com.aisino.admin.companyCard.cardBase.dao
 * @Class : CompanyCardBaseDao
 * @Description : CompanyCard建立用户权限所需的dao层接口
 * @author : liuya
 * @createDate : 2017-08-07 星期一 14:38:44
 * @version : V1.0.0
 * @Copyright : 2017 liuya Inc. All rights reserved.
 * @Reviewed : 
 * @UpateLog :    Name    Date    Reason/Contents
 */
public interface CompanyCardBaseDao {
    public List<Map<String,Object>> getCompany(UserModel user, String companyId) throws DAOException;
    public List getCmpIdByAuthority(UserModel user)throws DAOException;
    public List queryServiceId(List<String> companyIdList);
}
