package com.aisino.admin.companyCard.cardAudit.service.impl;

import com.aisino.admin.companyCard.cardAudit.bean.CompanyCardAudit;
import com.aisino.admin.companyCard.cardAudit.dao.CompanyCardAuditDao;
import com.aisino.admin.companyCard.cardAudit.service.CompanyCardAuditService;
import com.aisino.admin.companyCard.cardMaintain.bean.CmpCardApiResponse;
import com.aisino.admin.companyCard.cardMaintain.bean.CompanyCard;
import com.aisino.admin.companyCard.cardMaintain.utils.CmpCardApiUtils;
import com.aisino.admin.global.paging.Page;
import com.aisino.admin.global.utils.DateUtils;
import com.aisino.admin.global.utils.KafkaUtils;
import com.aisino.admin.global.utils.StringUtils;
import com.aisino.global.context.common.utils.SpringUtils;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

@Service("companyCardAuditService")
public class CompanyCardAuditServiceImpl implements CompanyCardAuditService {

    private final static String auditLogTopic = "dataAdmin";

    @Autowired
    @Qualifier("companyCardAuditDao")
    private CompanyCardAuditDao companyCardAuditDao;
    @Autowired
    @Qualifier("cmpJedisPool")
    private JedisPool cmpJedisPool;

    @Override
    public CompanyCardAudit get(Integer id) {
        return companyCardAuditDao.get(id);
    }

    @Override
    public void update(CompanyCardAudit cardAudit) {
        companyCardAuditDao.update(cardAudit);
    }

    @Override
    public void insert(CompanyCardAudit cardAudit) {
        companyCardAuditDao.insert(cardAudit);
    }

    @SuppressWarnings("rawtypes")
    private void removeAuditStatus(Map<String, Integer> code4audit) {
        if (code4audit != null) {
            Set<Entry<String, Integer>> set = code4audit.entrySet();
            for (Entry<String, Integer> entry : set) {
                if (companyCardAuditDao.checkCode(entry.getKey()) <= entry.getValue()) {
                    Jedis jedis = null;
                    try {
                        jedis = cmpJedisPool.getResource();
                        Map globalConfig = (Map) SpringUtils.getBean("globalConfig");
                        jedis.hdel((String) globalConfig.get("redis.prefixCard") + entry.getKey(), "audit");
                    } catch (Exception e) {
                    } finally {
                        if (jedis != null) {
                            jedis.close();
                            jedis = null;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean approve(int[] ids, String user) throws ParseException, IOException {
        boolean is_successful = true;
        List<CompanyCardAudit> list = companyCardAuditDao.queryByIds(ids);
        if (list != null && list.size() > 0) {
            Date auditTime = new Date();
            String auditTimeStr = DateUtils.format(auditTime);
            //取ip地址
            String ipaddr = getDataserverIP();
            List<String> batchLogs = new ArrayList<String>();
            String ids_str = "";
            Map<String, Integer> code4audit = null;
            for (CompanyCardAudit cardAudit : list) {
                boolean cur = true;
                CmpCardApiResponse rs = null;
                if (!StringUtils.isBlank(cardAudit.getCode())) {
                    CmpCardApiResponse response = CmpCardApiUtils.get(cardAudit.getCode());
                    if (response != null && response.getData() != null) {
                        cardAudit.setSource(cardAudit.compareSource(response.getData().getSource()));
                    }
                    String json = cardAudit.convertJson();
                    rs = CmpCardApiUtils.put(cardAudit.getCode(), json);
                    if (rs != null && rs.getCode() != null && rs.getCode().equals("201")) {
                        if (code4audit == null) {
                            code4audit = new HashMap<String, Integer>();
                        }
                        Integer count = code4audit.get(cardAudit.getCode());
                        if (count == null || count == 0) {
                            count = 1;
                        } else {
                            count++;
                        }
                        code4audit.put(cardAudit.getCode(), count);
                    }
                } else {
                    String json = cardAudit.convertJson();
                    rs = CmpCardApiUtils.post(json);
                }
                if (rs != null && rs.getCode() != null && rs.getCode().equals("201")) {
                } else {
                    is_successful = false;
                    cur = false;
                }
                if (cur) {
                    String log = convertAuditLog(cardAudit, true, user, auditTimeStr, ipaddr);
                    batchLogs.add(log);
                    ids_str += cardAudit.getId() + ",";
                }
            }
            if (batchLogs != null && batchLogs.size() > 0) {
                //发送日志
                try {
                    KafkaUtils.sendMessages(auditLogTopic, batchLogs);
                } finally {
                    KafkaUtils.close();
                }
            }
            if (code4audit != null) {
                removeAuditStatus(code4audit);
            }
            if (ids_str != null && ids_str.length() > 1) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("user", user);
                map.put("auditTimeStr", auditTimeStr);
                map.put("status", 1);
                map.put("ids", ids_str.substring(0, ids_str.length() - 1));
                companyCardAuditDao.approveList(map);
            }
        }
        return is_successful;
    }

    @Override
    public boolean notApprove(int[] ids, String user) {
        boolean is_successful = true;
        List<CompanyCardAudit> list = companyCardAuditDao.queryByIds(ids);
        if (list != null && list.size() > 0) {
            Date auditTime = new Date();
            String auditTimeStr = DateUtils.format(auditTime);
            //取ip地址
            String ipaddr = getDataserverIP();
            List<String> batchLogs = new ArrayList<String>();
            String ids_str = "";
            Map<String, Integer> code4audit = null;
            for (CompanyCardAudit cardAudit : list) {
                String log = convertAuditLog(cardAudit, false, user, auditTimeStr, ipaddr);
                batchLogs.add(log);
                if (!StringUtils.isBlank(cardAudit.getCode())) {
                    if (code4audit == null) {
                        code4audit = new HashMap<String, Integer>();
                    }
                    Integer count = code4audit.get(cardAudit.getCode());
                    if (count == null || count == 0) {
                        count = 1;
                    } else {
                        count++;
                    }
                    code4audit.put(cardAudit.getCode(), count);
                }
                ids_str += cardAudit.getId() + ",";
            }
            if (batchLogs != null && batchLogs.size() > 0) {
                //发送日志
                try {
                    KafkaUtils.sendMessages(auditLogTopic, batchLogs);
                } finally {
                    KafkaUtils.close();
                }
            }
            if (code4audit != null) {
                removeAuditStatus(code4audit);
            }
            if (ids_str != null && ids_str.length() > 1) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("user", user);
                map.put("auditTimeStr", auditTimeStr);
                map.put("status", -1);
                map.put("ids", ids_str.substring(0, ids_str.length() - 1));
                companyCardAuditDao.notApproveList(map);
            }
        }
        return is_successful;
    }

    @SuppressWarnings("rawtypes")
    private String getDataserverIP() {
        Map globalConfig = (Map) SpringUtils.getBean("globalConfig");
        return (String) globalConfig.get("dataserver.ip");
    }

    @Override
    public List<CompanyCardAudit> queryPage(Page<CompanyCardAudit> page) {
        return companyCardAuditDao.queryPage(page);
    }

    private String convertAuditLog(CompanyCardAudit cardAudit, boolean isApproved, String user, String auditTimeStr, String ipaddr) {
        StringBuilder sb = new StringBuilder();
        //code#taxid#name#address#telephone#bank#account#type#cert#source#user#auditResult#auditTime#ipaddr#datetime
        sb.append(cardAudit.getId()).append("#")
                .append(StringUtils.isBlank(cardAudit.getCode()) ? "" : cardAudit.getCode()).append("#")
                .append(StringUtils.isBlank(cardAudit.getTaxid()) ? "" : cardAudit.getTaxid()).append("#")
                .append(StringUtils.isBlank(cardAudit.getName()) ? "" : cardAudit.getName()).append("#")
                .append(StringUtils.isBlank(cardAudit.getAddress()) ? "" : cardAudit.getAddress()).append("#")
                .append(StringUtils.isBlank(cardAudit.getTelephone()) ? "" : cardAudit.getTelephone()).append("#")
                .append(StringUtils.isBlank(cardAudit.getBank()) ? "" : cardAudit.getBank()).append("#")
                .append(StringUtils.isBlank(cardAudit.getAccount()) ? "" : cardAudit.getAccount()).append("#")
                .append(cardAudit.getType() == null ? "" : cardAudit.getType()).append("#")
                .append(cardAudit.getCert() == null ? "" : cardAudit.getCert()).append("#")
                .append(cardAudit.getSource() == null ? "" : cardAudit.getSource()).append("#")
                .append(user).append("#").append(isApproved ? "1" : "0").append("#")
                .append(auditTimeStr).append("#").append(ipaddr).append("#")
                .append(DateUtils.format(cardAudit.getCreateTime()));
        return sb.toString();
    }

    @Override
    public int count(Page<CompanyCardAudit> page) {
        return companyCardAuditDao.count(page);
    }

    @Override
    public List<CompanyCardAudit> queryPage4Metching(Page<CompanyCardAudit> page) {
        return companyCardAuditDao.queryPage4Metching(page);
    }

    @Override
    public int count4Metching(Page<CompanyCardAudit> page) {
        return companyCardAuditDao.count4Metching(page);
    }

    @Override
    public void changeCert(CompanyCardAudit cardAudit) {
        companyCardAuditDao.changeCert(cardAudit);
    }

    @Override
    public boolean merge(int auditId, String code, String user, int type) throws ClientProtocolException, IOException {
        CompanyCardAudit cardAudit = companyCardAuditDao.get(auditId);
        CmpCardApiResponse apiResponse = CmpCardApiUtils.get(code);
        CompanyCard card = apiResponse.getData();
        if (cardAudit != null && card != null) {
            //验证code
            if (type == 1) {//验证name
                if (!StringUtils.isBlank(cardAudit.getName())
                        && cardAudit.getName().equals(card.getName())) {
                } else {
                    return false;
                }
            } else if (type == 2) { //验证taxid
                if (!StringUtils.isBlank(cardAudit.getTaxid())
                        && cardAudit.getTaxid().equals(card.getTaxid())) {
                } else {
                    return false;
                }
            }
            if (!StringUtils.isBlank(cardAudit.getName())) {
                card.setName(cardAudit.getName());
            }
            if (!StringUtils.isBlank(cardAudit.getAccount())) {
                card.setAccount(cardAudit.getAccount());
            }
            if (!StringUtils.isBlank(cardAudit.getAddress())) {
                card.setAddress(cardAudit.getAddress());
            }
            if (!StringUtils.isBlank(cardAudit.getBank())) {
                card.setBank(cardAudit.getBank());
            }
            if (!StringUtils.isBlank(cardAudit.getTaxid())) {
                card.setTaxid(cardAudit.getTaxid());
            }
            if (!StringUtils.isBlank(cardAudit.getTelephone())) {
                card.setTelephone(cardAudit.getTelephone());
            }
            if (cardAudit.getType() != null) {
                card.setType(cardAudit.getType());
            }
            if (cardAudit.getCert() != null) {
                card.setCert(cardAudit.getCert());
            }
            if (cardAudit.getSource() != null && compareSoures(cardAudit.getSource() + "",
                    card.getSource() + "") > 0) {
                card.setSource(cardAudit.getSource());
            }
            CmpCardApiUtils.put(code, card.convertJson());
            Date auditTime = new Date();
            String auditTimeStr = DateUtils.format(auditTime);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user", user);
            map.put("auditTimeStr", auditTimeStr);
            map.put("status", 1);
            map.put("ids", auditId);
            companyCardAuditDao.approveList(map);
            List<String> batchLogs = new ArrayList<String>();
            String ipaddr = getDataserverIP();
            String log = convertAuditLog(cardAudit, true, user, auditTimeStr, ipaddr);
            batchLogs.add(log);
            //发送日志
            try {
                KafkaUtils.sendMessages(auditLogTopic, batchLogs);
            } finally {
                KafkaUtils.close();
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 来源比较
     *
     * @param s1
     * @param s2
     * @return
     */
    private static int compareSoures(String s1, String s2) {

        if (s1 == null) {
            return s2 == null ? 0 : -1;
        }
        if (s2 == null) {
            return 1;
        }
        if ((s1.equals("10")) || (s1.equals("11"))) {
            if ((s2.equals("10")) || (s2.equals("11"))) {
                return 0;
            }
            return 1;
        }
        if ((s1.equals("30")) || (s1.equals("32")) || (s1.equals("33")) || (s1.equals("40")) || (s1.equals("99"))) {
            if ((s2.equals("10")) || (s2.equals("11"))) {
                return -1;
            }
            if ((s2.equals("30")) || (s2.equals("32")) || (s2.equals("33")) || (s2.equals("40")) || (s2.equals("99"))) {
                return 0;
            }
            return 1;
        }
        if (s1.equals("12")) {
            if ((s2.equals("10")) || (s2.equals("11")) || (s2.equals("30")) || (s2.equals("32")) || (s2.equals("33")) || (s2.equals("40")) || (s2.equals("99"))) {
                return -1;
            }
            if (s2.equals("12")) {
                return 0;
            }
            return 1;
        }
        if ((s1.equals("50")) || (s1.equals("31"))) {
            if ((s2.equals("10")) || (s2.equals("11")) || (s2.equals("30")) || (s2.equals("32")) || (s2.equals("33")) || (s2.equals("40")) || (s2.equals("99")) ||
                    (s2.equals("12"))) {
                return -1;
            }
            if ((s2.equals("50")) || (s2.equals("31"))) {
                return 0;
            }
            return 1;
        }
        if ((s1.equals("20")) || (s1.equals("0"))) {
            if ((s2.equals("20")) || (s2.equals("0"))) {
                return 0;
            }
            return -1;
        }
        if ((s2.equals("20")) || (s2.equals("0"))) {
            return 0;
        }
        return -1;
    }

}
