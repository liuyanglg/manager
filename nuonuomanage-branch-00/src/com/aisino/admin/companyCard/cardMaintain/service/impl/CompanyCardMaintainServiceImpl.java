package com.aisino.admin.companyCard.cardMaintain.service.impl;

import com.aisino.admin.companyCard.cardMaintain.bean.CmpCardApiResponse;
import com.aisino.admin.companyCard.cardMaintain.bean.CompanyCard;
import com.aisino.admin.companyCard.cardMaintain.dao.CompanyCardMaintainDao;
import com.aisino.admin.companyCard.cardMaintain.service.CompanyCardMaintainService;
import com.aisino.admin.companyCard.cardMaintain.utils.CmpCardApiUtils;
import com.aisino.admin.global.paging.Page;
import com.aisino.admin.global.utils.DateUtils;
import com.aisino.global.context.common.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("companyCardMaintainService")
public class CompanyCardMaintainServiceImpl implements CompanyCardMaintainService {

    @Autowired
    @Qualifier("companyCardMaintainDao")
    private CompanyCardMaintainDao companyCardMaintainDao;
    @Autowired
    @Qualifier("cmpJedisPool")
    private JedisPool cmpJedisPool;

    @Override
    public List<CompanyCard> queryPage(Page<CompanyCard> page) {
        return companyCardMaintainDao.queryPage(page);
    }

    @Override
    public int count(Page<CompanyCard> page) {
        return companyCardMaintainDao.count(page);
    }

    @Override
    public CompanyCard queryByName(String name) {
        return companyCardMaintainDao.queryByName(name);
    }

    @Override
    public CompanyCard queryByCode(String code) {
        return companyCardMaintainDao.queryByCode(code);
    }

    @Override
    public CompanyCard queryById(int id) {
        return companyCardMaintainDao.queryById(id);
    }

    @Override
    public CompanyCard queryByTaxid(String taxid) {
        return companyCardMaintainDao.queryByTaxid(taxid);
    }

    /**
     * 对每个用户进行频率控制，频率可设置,另外每天每个用户限制删除总量，总量也可配置
     */
    @SuppressWarnings("unchecked")
    public boolean delete(String[] codes, String user) {
        boolean s = true;
        if (codes != null && codes.length > 0) {
            if (codes.length > 3) {
                return false;
            }
            Date cur = new Date();
            Jedis jedis = null;
            int deleted = 0;
            int user_day_deleted = 0;
            Map<String, String> globalConfig = (Map<String, String>) SpringUtils.getBean("globalConfig");
            String user_key = globalConfig.get("redis.cardDeletePrefix") + user + DateUtils.format(cur, "yyyy-MM-dd");
            try {
                jedis = cmpJedisPool.getResource();
                int frequency_sec = Integer.valueOf(globalConfig.get("card.delete.frequency"));
                int total = Integer.valueOf(globalConfig.get("card.delete.total"));
                Map user_pre_del = jedis.hgetAll(user_key);
                if (user_pre_del != null) {
                    String pre_time = (String) user_pre_del.get("pre_del_time");
                    if (pre_time != null) {
                        long pl = Long.valueOf(pre_time);
                        if ((cur.getTime() - pl) <= (frequency_sec * 1000)) {
                            return false;
                        }
                    }
                    String day_total = (String) user_pre_del.get("day_del_total");
                    if (day_total != null) {
                        user_day_deleted = Integer.valueOf(day_total);
                        if (user_day_deleted + codes.length > total) {
                            return false;
                        }
                    }
                    for (String code : codes) {
                        CmpCardApiResponse apiResponse = CmpCardApiUtils.delete(code);
                        if (apiResponse != null && "204".equals(apiResponse.getCode())) {
                            deleted++;
                        } else {
                            s = false;
                        }
                    }
                    user_pre_del.put("pre_del_time", String.valueOf(cur.getTime()));
                    user_pre_del.put("day_del_total", String.valueOf(user_day_deleted + deleted));
                    jedis.hmset(user_key, user_pre_del);
                } else {
                    for (String code : codes) {
                        CmpCardApiResponse apiResponse = CmpCardApiUtils.delete(code);
                        if (apiResponse != null && "204".equals(apiResponse.getCode())) {
                            deleted++;
                        } else {
                            s = false;
                        }
                    }
                    user_pre_del = new HashMap<String, Object>();
                    user_pre_del.put("pre_del_time", cur.getTime());
                    user_pre_del.put("day_del_total", deleted);
                    jedis.hmset(user_key, user_pre_del);
                    jedis.expireAt(user_key, DateUtils.todayEnd().getTime());
                }
            } catch (Exception e) {
                if (deleted > 0) {
                    Map user_pre_del = new HashMap<String, Object>();
                    user_pre_del = new HashMap<String, Object>();
                    user_pre_del.put("pre_del_time", cur.getTime());
                    user_pre_del.put("day_del_total", user_day_deleted + deleted);
                    jedis.hmset(user_key, user_pre_del);
                    jedis.expireAt(user_key, DateUtils.todayEnd().getTime());
                }
                e.printStackTrace();
                s = false;
            } finally {
                if (jedis != null) {
                    jedis.close();
                    jedis = null;
                }
            }
        }
        return s;
    }

    @SuppressWarnings("rawtypes")
    public void update(CompanyCard card){
        companyCardMaintainDao.update(card);
        Jedis jedis = null;
        try {
            jedis = cmpJedisPool.getResource();
            Map globalConfig = (Map) SpringUtils.getBean("globalConfig");
            String key = (String) globalConfig.get("redis.prefixCard") + card.getCode();
            Pipeline pipeline = jedis.pipelined();
            if (card.getCode() != null) {
                pipeline.hset(key, "code", card.getCode());
            }
            if (card.getName() != null) {
                pipeline.hset(key, "name", card.getName());
            }
            if (card.getSource() != null) {
                pipeline.hset(key, "source", String.valueOf(card.getSource()));
            }
            if (card.getType() != null) {
                pipeline.hset(key, "type", String.valueOf(card.getType()));
            }
            if (card.getCert() != null) {
                pipeline.hset(key, "cert", String.valueOf(card.getCert()));
            }
            if (card.getTelephone() != null) {
                pipeline.hset(key, "telephone", card.getTelephone());
            }
            if (card.getAddress() != null) {
                pipeline.hset(key, "address", card.getAddress());
            }
            if (card.getBank() != null) {
                pipeline.hset(key, "bank", card.getBank());
            }
            if (card.getAccount() != null) {
                pipeline.hset(key, "account", card.getAccount());
            }
            if (card.getStatus() != null) {
                pipeline.hset(key, "status", String.valueOf(card.getStatus()));
            }
            pipeline.sync();
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }
}
