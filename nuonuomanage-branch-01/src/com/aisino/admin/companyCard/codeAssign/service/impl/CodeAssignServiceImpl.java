package com.aisino.admin.companyCard.codeAssign.service.impl;

import com.aisino.admin.companyCard.codeAssign.bean.KPAssignCodeVO;
import com.aisino.admin.companyCard.codeAssign.bean.KPCodeDetailDO;
import com.aisino.admin.companyCard.codeAssign.bean.KPCodeMainDO;
import com.aisino.admin.companyCard.codeAssign.dao.CodeAssignDao;
import com.aisino.admin.companyCard.codeAssign.dao.CodeDetailDao;
import com.aisino.admin.companyCard.codeAssign.dao.CodeMainDao;
import com.aisino.admin.companyCard.codeAssign.service.CodeAssignService;
import com.aisino.admin.companyCard.codeAssign.utils.AssignApiUtils;
import com.aisino.admin.global.paging.Page;
import com.aisino.admin.global.utils.StringUtils;
import com.aisino.global.context.common.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.*;

@Service("codeAssignService")
public class CodeAssignServiceImpl implements CodeAssignService {
    @Autowired
    CodeAssignDao codeAssignDao;

    @Autowired
    CodeMainDao codeMainDao;

    @Autowired
    CodeDetailDao codeDetailDao;

    @Autowired
    @Qualifier("cmpJedisPool")
    private JedisPool cmpJedisPool;

    public List<KPAssignCodeVO> queryPage(Page<KPAssignCodeVO> page) {
        return codeAssignDao.queryPage(page);
    }

    public int count(Page<KPAssignCodeVO> page) {
        return codeAssignDao.count(page);
    }

    public List<Map<String, Object>> queryEmployeeList(String taxid) {
        List<Map<String, Object>> employeeList = null;
        try {
            Map<String, String> companyMap = AssignApiUtils.queryCompanyIdApi(taxid);
            String companyId = "";
            if (companyMap != null) {
                companyId = companyMap.get("CUid");
            }
            employeeList = AssignApiUtils.queryEmployeeListApi(companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (employeeList == null) {
            employeeList = new ArrayList<Map<String, Object>>();
        }
        return convertEmployees(employeeList);
    }

    public boolean assignCodes(KPCodeMainDO kpCodeMainDO,KPCodeDetailDO kpCodeDetailDO) {
        boolean success = true;
        if (kpCodeMainDO == null) {
            return false;
        }
        if (StringUtils.isBlank(kpCodeMainDO.getAssignCompanyTaxid())) {
            return false;
        }
        if (StringUtils.isBlank(kpCodeMainDO.getAssignCompanyName())) {
            return false;
        }
        try {
            /*从redis里获取相应数量的六位代码*/
            Set<String> fetchCodeSet = fetchCodes(kpCodeMainDO.getAssignAmount());
            /*缓存中可能不够，size为实际获取数量*/
            int size = fetchCodeSet.size();
            if (size <= 0) {
                return false;
            }
            String mobile = null;
            if (kpCodeDetailDO.getAssignEmployeeId() != null && kpCodeDetailDO.getAssignEmployeeId().trim().length() > 0) {
                Map<String, Object> employee = getEmployeeById(kpCodeMainDO.getAssignCompanyTaxid(), kpCodeDetailDO.getAssignEmployeeId());
                if (employee != null) {
                    mobile = (String) employee.get("assignEmployeeMobile");
                }
            }
            kpCodeDetailDO.setAssignEmployeeMobile(mobile);

            KPCodeMainDO dbCodeMainDO = codeMainDao.queryByTaxid(kpCodeMainDO.getAssignCompanyTaxid());
            Integer codeMainId = null;
            if (dbCodeMainDO != null) {
                /*分初次分配进行更新，分配数量累加*/
                codeMainId = dbCodeMainDO.getId();
                int amount = dbCodeMainDO.getAssignAmount();
                dbCodeMainDO.setAssignAmount(amount + size);
                codeMainDao.update(dbCodeMainDO);
            } else {
                /*初次分配进行插入，分配数量为实际获取六位代码数量*/
                kpCodeMainDO.setAssignAmount(size);
                codeMainDao.insert(kpCodeMainDO);
                codeMainId = kpCodeMainDO.getId();
            }

            List<KPCodeDetailDO> kpCodeDetailDOList = new ArrayList<KPCodeDetailDO>();
            for (String code : fetchCodeSet) {
                KPCodeDetailDO insertDetailDO = new KPCodeDetailDO();
                insertDetailDO.setMainId(codeMainId);
                insertDetailDO.setPreAssignCode(code);
                insertDetailDO.setAssignEmployeeId(kpCodeDetailDO.getAssignEmployeeId());
                insertDetailDO.setAssignEmployeeName(kpCodeDetailDO.getAssignEmployeeName());
                insertDetailDO.setAssignEmployeeMobile(kpCodeDetailDO.getAssignEmployeeMobile());
                insertDetailDO.setStatus("0");
                kpCodeDetailDOList.add(insertDetailDO);
            }

            if (kpCodeDetailDOList.size() > 0) {
                codeDetailDao.insertBatch(kpCodeDetailDOList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * @method : fetchCodes
     * @description : 从redis拿指定数量的六位代码
     * @param size :  
     * @return : java.util.Set<java.lang.String>
     * @author : liuya
     * @date : 2017-10-24 星期二 18:16:22
     */
    public Set<String> fetchCodes(int size) {
        Set<String> codeSet = new HashSet<String>();
        Jedis jedis = null;
        try {
            Map globalConfig = (Map) SpringUtils.getBean("globalConfig");
            String redisKey = (String) globalConfig.get("code.prepare.assign.pool");
            JedisPool cmpJedisPool=(JedisPool) SpringUtils.getBean("cmpJedisPool");
            jedis = cmpJedisPool.getResource();
            Set<Response<String>> responseSet = new HashSet<Response<String>>();
            Pipeline pipeline = jedis.pipelined();
            long remainFetchSize = size;

            while (remainFetchSize>0) {
                long batchSize=100;
                if (remainFetchSize<batchSize) {
                    batchSize = remainFetchSize;
                }
                Response<Set<String>> response = pipeline.spop(redisKey,batchSize);
                if (response == null) {
                    break;
                }
                pipeline.sync();

                Set<String> fetchSet = response.get();
                codeSet.addAll(fetchSet);
                remainFetchSize =remainFetchSize- fetchSet.size();
                if(fetchSet.size()<batchSize){
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return codeSet;
    }

    public List<Map<String, Object>> convertEmployees(List<Map<String, Object>> employeeList) {
        List<Map<String, Object>> convertList = new ArrayList<Map<String, Object>>();
        if (employeeList == null) {
            return convertList;
        }
        if (employeeList.size() <= 0) {
            return convertList;
        }
        for (Map<String, Object> item : employeeList) {
            Map<String, Object> employeeMap = new HashMap<String, Object>();
            employeeMap.put("assignEmployeeId", item.get("userId"));
            employeeMap.put("assignEmployeeName", item.get("name"));
            employeeMap.put("assignEmployeeMobile", item.get("mobile"));
            convertList.add(employeeMap);
        }
        return convertList;
    }

    public Map<String, Object> getEmployeeById(String taxid, String assignEmployeeId) {
        List<Map<String, Object>> list = queryEmployeeList(taxid);
        Map<String, Object> employee = null;
        for (Map<String, Object> item : list) {
            String id = (String) item.get("assignEmployeeId");
            if (id != null) {
                assignEmployeeId.equals(id);
                employee = item;
                return employee;
            }
        }
        return employee;
    }
}
