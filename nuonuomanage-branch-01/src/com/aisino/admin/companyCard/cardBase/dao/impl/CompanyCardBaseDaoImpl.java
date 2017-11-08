package com.aisino.admin.companyCard.cardBase.dao.impl;

import com.aisino.admin.companyCard.cardBase.dao.CompanyCardBaseDao;
import com.aisino.global.context.common.dao.BaseJdbcDAO;
import com.aisino.global.context.common.ex.DAOException;
import com.aisino.global.context.common.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("companyCardBaseDao")
public class CompanyCardBaseDaoImpl implements CompanyCardBaseDao {
    @Autowired
    BaseJdbcDAO baseJdbcDAO;

    /**
     * @Method : getCompany
     * @Description :该方法从frameProject模块拷贝，根据用户类型获取companyId
     * @param user :
     * @param companyId :
     * @return : java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author : liuya
     * @createDate : 2017-08-07 星期一 14:40:56
     */
    @Override
    public List<Map<String, Object>> getCompany(UserModel user, String companyId) throws DAOException {
        int expand1 = 0, expand2 = 0;//修改设置选中的公司节点都展开
        //省级
        String sql = " SELECT c_subid as id,c_subname as text,'closed' as state FROM portal_choose_subcompany  WHERE c_parentid is null  ";
        if (user.getUserType().toString().contains("USERBROKER")) {
            sql = "SELECT c_subid as id,c_subname as text,'closed' as state FROM `portal_choose_subcompany` WHERE c_subid IN"
                    + "(SELECT b.c_parentid FROM portal_choose_subcompany a,portal_choose_subcompany b "
                    + "WHERE a.c_parentid=b.c_subid AND a.c_subid='" + user.getCompanyid() + "')";
        } else if (user.getUserType().toString().contains("USER")) {
            sql = "SELECT c_subid as id,c_subname as text,'closed' as state FROM `portal_choose_subcompany` WHERE c_subid IN"
                    + "(SELECT a.c_parentid FROM `global_company` g ,portal_choose_subcompany s,"
                    + "portal_choose_subcompany a WHERE  g.c_companyname=s.c_subname "
                    + "AND s.c_parentid=a.c_subid AND c_ownerid='" + user.getUserid() + "' )";
        }

        List<Map<String, Object>> list = baseJdbcDAO.getJdbcTemplate().queryForList(sql);
        for (Map<String, Object> map : list) {
            //市级
            String sql2 = " SELECT c_subid as id,c_subname as text ,'closed' as state FROM portal_choose_subcompany WHERE c_parentid ='" + map.get("id").toString() + "' ";
            if (user.getUserType().toString().contains("USERBROKER")) {
                sql2 = "SELECT b.c_subid as id,b.c_subname as text,'closed' as state FROM portal_choose_subcompany a,portal_choose_subcompany b "
                        + "WHERE a.c_parentid=b.c_subid AND a.c_subid='" + user.getCompanyid() + "'";
            } else if (user.getUserType().toString().contains("USER")) {
                sql2 = "SELECT DISTINCT a.c_subid AS id,a.c_subname AS text,'closed' as state FROM global_company g ,portal_choose_subcompany s,"
                        + "portal_choose_subcompany a WHERE  g.c_companyname=s.c_subname "
                        + "AND s.c_parentid=a.c_subid AND g.c_ownerid='" + user.getUserid() + "' ";
            }

            List<Map<String, Object>> list2 = baseJdbcDAO.getJdbcTemplate().queryForList(sql2);
            if (list2 != null && list2.size() > 0) {
                //市级下面公司
                for (Map<String, Object> map2 : list2) {
                    String sql3 = " SELECT c_subid as id,c_subname as text ,'open' as state FROM portal_choose_subcompany WHERE c_parentid ='" + map2.get("id").toString() + "' ";
                    if (user.getUserType().toString().contains("USERBROKER")) {
                        sql3 = "SELECT a.c_subid as id,a.c_subname as text ,'open' as state FROM portal_choose_subcompany a,portal_choose_subcompany b "
                                + "WHERE a.c_parentid=b.c_subid AND a.c_subid='" + user.getCompanyid() + "'";
                    }
                    List<Map<String, Object>> list3 = baseJdbcDAO.getJdbcTemplate().queryForList(sql3);
                    if (list3 != null && list3.size() > 0) {
                        for (Map<String, Object> map3 : list3) {
                            if (map3.containsValue(companyId)) {
                                expand1 = 1;
                                expand2 = 1;
                                break;
                            }
                        }
                        map2.put("children", list3);
                        if (expand1 == 1 && companyId != null) {
                            map2.put("state", "open");
                            expand1 = 0;
                        }
                    }
                }
                map.put("children", list2);
                if (expand2 == 1 && companyId != null) {
                    map.put("state", "open");
                    expand2 = 0;
                }
            }
        }
        return list;
    }

    @Override
    public List getCmpIdByAuthority(UserModel user) throws DAOException {
        Set<String> set = new HashSet<String>();
        List<String> list = new ArrayList<String>();
        //省级
        String sql = " SELECT c_subid as id FROM portal_choose_subcompany  WHERE c_parentid is null  ";
        if (user.getUserType().toString().contains("USERBROKER")) {
            sql = "SELECT c_subid as id FROM `portal_choose_subcompany` WHERE c_subid IN"
                    + "(SELECT b.c_parentid FROM portal_choose_subcompany a,portal_choose_subcompany b "
                    + "WHERE a.c_parentid=b.c_subid AND a.c_subid='" + user.getCompanyid() + "')";
        } else if (user.getUserType().toString().contains("USER")) {
            sql = "SELECT c_subid as id FROM `portal_choose_subcompany` WHERE c_subid IN"
                    + "(SELECT a.c_parentid FROM `global_company` g ,portal_choose_subcompany s,"
                    + "portal_choose_subcompany a WHERE  g.c_companyname=s.c_subname "
                    + "AND s.c_parentid=a.c_subid AND c_ownerid='" + user.getUserid() + "' )";
        }

        List<String> listProvince = baseJdbcDAO.getJdbcTemplate().queryForList(sql, String.class);
        set.addAll(listProvince);
        for (String str : listProvince) {
            //市级
            String sql2 = " SELECT c_subid as id FROM portal_choose_subcompany WHERE c_parentid ='" + str + "' ";
            if (user.getUserType().toString().contains("USERBROKER")) {
                sql2 = "SELECT b.c_subid as id FROM portal_choose_subcompany a,portal_choose_subcompany b "
                        + "WHERE a.c_parentid=b.c_subid AND a.c_subid='" + user.getCompanyid() + "'";
            } else if (user.getUserType().toString().contains("USER")) {
                sql2 = "SELECT DISTINCT a.c_subid AS id FROM global_company g ,portal_choose_subcompany s,"
                        + "portal_choose_subcompany a WHERE  g.c_companyname=s.c_subname "
                        + "AND s.c_parentid=a.c_subid AND g.c_ownerid='" + user.getUserid() + "' ";
            }

            List<String> listCity = baseJdbcDAO.getJdbcTemplate().queryForList(sql2, String.class);
            set.addAll(listCity);
            if (listCity != null && listCity.size() > 0) {
                //市级下面公司
                for (String str2 : listCity) {
                    String sql3 = " SELECT c_subid as id FROM portal_choose_subcompany WHERE c_parentid ='" + str2.toString() + "' ";
                    if (user.getUserType().toString().contains("USERBROKER")) {
                        sql3 = "SELECT a.c_subid as id FROM portal_choose_subcompany a,portal_choose_subcompany b "
                                + "WHERE a.c_parentid=b.c_subid AND a.c_subid='" + user.getCompanyid() + "'";
                    }
                    List<String> listCounty = baseJdbcDAO.getJdbcTemplate().queryForList(sql3, String.class);
                    set.addAll(listCounty);
                }
            }
        }
        list.addAll(set);
        return list;
    }


    @Override
    public List queryServiceId(List<String> companyIdList) {
        List<String> serviceIdList = null;
        if (companyIdList == null || companyIdList.size() == 0) {
            return serviceIdList;
        }

        StringBuffer sql = new StringBuffer("SELECT DISTINCT " +
                "g.`c_organizationid` AS servieid  " +
                "FROM global_company g " +
                "WHERE g.`c_companyid` IN(");
        int len = sql.length();
        for (String id : companyIdList) {
            sql.append("'" + id + "',");
        }
        if (sql.length() > len) {
            sql = sql.deleteCharAt(sql.length() - 1);
        }
        sql.append(");");
        serviceIdList = baseJdbcDAO.getJdbcTemplate().queryForList(sql.toString(), String.class);
        return serviceIdList;
    }
}
