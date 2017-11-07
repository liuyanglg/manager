package com.aisino.admin.companyCard.cardAudit.service.impl;

import com.aisino.admin.companyCard.cardAudit.bean.CompanyCardAudit;
import com.aisino.admin.companyCard.cardAudit.dao.CompanyCardAuditDao;
import com.aisino.admin.companyCard.cardAudit.service.CompanyCardAuditService;
import com.aisino.admin.companyCard.cardBase.service.CompanyCardBaseService;
import com.aisino.admin.companyCard.cardMaintain.bean.CmpCardApiResponse;
import com.aisino.admin.companyCard.cardMaintain.bean.CompanyCard;
import com.aisino.admin.companyCard.cardMaintain.utils.CmpCardApiUtils;
import com.aisino.admin.global.paging.Page;
import com.aisino.admin.global.utils.DateUtils;
import com.aisino.admin.global.utils.KafkaUtils;
import com.aisino.admin.global.utils.StringUtils;
import com.aisino.global.context.common.model.UserModel;
import com.aisino.global.context.common.utils.SpringUtils;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Service("companyCardAuditService")
public class CompanyCardAuditServiceImpl implements CompanyCardAuditService {
    private final static String auditLogTopic = "dataAdmin";

    @Autowired
    @Qualifier("companyCardBaseService")
    private CompanyCardBaseService companyCardBaseService;
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


    @Override
    public List<CompanyCardAudit> queryPage(Page<CompanyCardAudit> page) {
        return companyCardAuditDao.queryPage(page);
    }

    @Override
    public List<CompanyCardAudit> queryPage(Page<CompanyCardAudit> page, UserModel user, String companyId) {
        companyId = convertCompanyId(companyId);
        String userType = user.getUserType().toString();
        Map<String, Object> paramsMap = companyCardBaseService.getServiceIdMap(user, companyId);
        page.setOtherParams(paramsMap);//将服务单位ID放入Page
            /*超级用户默认查询所有数据*/
        if (userType.contains("SUPER")) {
            if (companyId.trim().length() == 0 || companyId == null) {
                if (page.getOtherParams() != null) {
                    page.setOtherParams(null);//设置为空值，MyBatis就不加限定条件了
                }
            }
        } else {
                /*普通用户查询具有权限的数据*/
            page.setOtherParams(paramsMap);//将服务单位ID放入Page
        }
        return companyCardAuditDao.queryPage(page);
    }

    @Override
    public int count(Page<CompanyCardAudit> page) {
        return companyCardAuditDao.count(page);
    }

    @Override
    public int count(Page<CompanyCardAudit> page, UserModel user, String companyId) {
        companyId = convertCompanyId(companyId);
        String userType = user.getUserType().toString();
        Map<String, Object> paramsMap = companyCardBaseService.getServiceIdMap(user, companyId);
        page.setOtherParams(paramsMap);//将服务单位ID放入Page
            /*超级用户默认查询所有数据*/
        if (userType.contains("SUPER")) {
            if (companyId.trim().length() == 0 || companyId == null) {
                if (page.getOtherParams() != null) {
                    page.setOtherParams(null);//设置为空值，MyBatis就不加限定条件了
                }
            }
        } else {
                /*普通用户查询具有权限的数据*/
            page.setOtherParams(paramsMap);//将服务单位ID放入Page
        }
        return companyCardAuditDao.count(page);
    }

    @Override
    public List<CompanyCardAudit> queryPage4Metching(Page<CompanyCardAudit> page, UserModel user, String companyId) {
        companyId = convertCompanyId(companyId);
        String userType = user.getUserType().toString();
        Map<String, Object> paramsMap = companyCardBaseService.getServiceIdMap(user, companyId);
        page.setOtherParams(paramsMap);//将服务单位ID放入Page
            /*超级用户默认查询所有数据*/
        if (userType.contains("SUPER")) {
            if (companyId.trim().length() == 0 || companyId == null) {
                if (page.getOtherParams() != null) {
                    page.setOtherParams(null);//设置为空值，MyBatis就不加限定条件了
                }
            }
        } else {
                /*普通用户查询具有权限的数据*/
            page.setOtherParams(paramsMap);//将服务单位ID放入Page
        }
        return companyCardAuditDao.queryPage4Metching(page);
    }

    @Override
    public int count4Metching(Page<CompanyCardAudit> page, UserModel user, String companyId) {
        companyId = convertCompanyId(companyId);
        String userType = user.getUserType().toString();
        Map<String, Object> paramsMap = companyCardBaseService.getServiceIdMap(user, companyId);
        page.setOtherParams(paramsMap);//将服务单位ID放入Page
            /*超级用户默认查询所有数据*/
        if (userType.contains("SUPER")) {
            if (companyId.trim().length() == 0 || companyId == null) {
                if (page.getOtherParams() != null) {
                    page.setOtherParams(null);//设置为空值，MyBatis就不加限定条件了
                }
            }
        } else {
                /*普通用户查询具有权限的数据*/
            page.setOtherParams(paramsMap);//将服务单位ID放入Page
        }
        return companyCardAuditDao.count4Metching(page);
    }

    @Override
    public void exportList(Page<CompanyCardAudit> page, HttpServletResponse response, UserModel user, String companyId) {
        List<CompanyCardAudit> list = queryPage(page, user, companyId);
        exportExcel(list, response);
    }

    @Override
    public void exportMatch(Page<CompanyCardAudit> page, HttpServletResponse response, UserModel user, String companyId) {
        List<CompanyCardAudit> list = queryPage4Metching(page, user, companyId);
        exportExcel(list, response);
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
        if ((s1.equals("30")) || (s1.equals("32")) || (s1.equals("40")) || (s1.equals("99"))) {
            if ((s2.equals("10")) || (s2.equals("11"))) {
                return -1;
            }
            if ((s2.equals("30")) || (s2.equals("32")) || (s2.equals("40")) || (s2.equals("99"))) {
                return 0;
            }
            return 1;
        }
        if (s1.equals("12")) {
            if ((s2.equals("10")) || (s2.equals("11")) || (s2.equals("30")) || (s2.equals("32")) || (s2.equals("40")) || (s2.equals("99"))) {
                return -1;
            }
            if (s2.equals("12")) {
                return 0;
            }
            return 1;
        }
        if ((s1.equals("50")) || (s1.equals("31"))) {
            if ((s2.equals("10")) || (s2.equals("11")) || (s2.equals("30")) || (s2.equals("32")) || (s2.equals("40")) || (s2.equals("99")) ||
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

    @SuppressWarnings("rawtypes")
    public String convertCompanyId(String companyId) {
        String convertResult = "";
        if (companyId != null) {
            StringBuffer buffer = new StringBuffer();
            String[] ids = companyId.split(",");
            for (String s : ids) {
                buffer.append("\'" + s.trim() + "\'" + ",");
            }
            if (buffer.length() > 0) {
                convertResult = buffer.substring(0, buffer.length() - 1);
            }
        }
        return convertResult;
    }

    @SuppressWarnings("rawtypes")
    public void exportExcel(List<CompanyCardAudit> list, HttpServletResponse response) {
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("开票信息审核列表");
        Row row = sheet.createRow((int) 0);
        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER); // 创建一个居中格式
        Cell cell = null;
        String[] headers = {"操作类型", "数据来源", "审核状态", "六位代码", "企业名称", "税号", "地址", "电话", "开户行", "银行账号", "认证状态", "创建时间", "纳税人标识", "审核人", "审核时间"};
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                CompanyCardAudit audit = list.get(i);
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(StringUtils.isBlank(audit.getCode()) ? "新增" : "修改");
                row.createCell(1).setCellValue(audit.getSourceStr());
                row.createCell(2).setCellValue(audit.getStatusStr());
                row.createCell(3).setCellValue(audit.getCode());
                row.createCell(4).setCellValue(audit.getName());
                row.createCell(5).setCellValue(audit.getTaxid());
                row.createCell(6).setCellValue(audit.getAddress());
                row.createCell(7).setCellValue(audit.getTelephone());
                row.createCell(8).setCellValue(audit.getBank());
                row.createCell(9).setCellValue(audit.getAccount());
                row.createCell(10).setCellValue(audit.getCertStr());
                row.createCell(11).setCellValue(audit.getCreateTimeStr());
                row.createCell(12).setCellValue(audit.getTypeStr());
                row.createCell(13).setCellValue(audit.getAuditor());
                row.createCell(14).setCellValue(audit.getAuditTimeStr());
            }
        }
        String fileName = "kpInfoAudit_" + (new SimpleDateFormat("yyyyMMddHH").format(new Date())) + ".xlsx";
        OutputStream os = null;
        try {
            os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
            response.setContentType("application/x-download");
            wb.write(os);
            os.flush();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != wb) {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    @SuppressWarnings("rawtypes")
    private String getDataserverIP() {
        Map globalConfig = (Map) SpringUtils.getBean("globalConfig");
        return (String) globalConfig.get("dataserver.ip");
    }

    @SuppressWarnings("rawtypes")
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
}
