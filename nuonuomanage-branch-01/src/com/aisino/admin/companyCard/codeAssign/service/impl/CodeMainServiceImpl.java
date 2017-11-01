package com.aisino.admin.companyCard.codeAssign.service.impl;

import com.aisino.admin.companyCard.codeAssign.bean.KPCodeMainDO;
import com.aisino.admin.companyCard.codeAssign.dao.CodeMainDao;
import com.aisino.admin.companyCard.codeAssign.service.CodeMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("codeMainService")
public class CodeMainServiceImpl implements CodeMainService {
    @Autowired
    CodeMainDao codeMainDao;

    public KPCodeMainDO get(Integer id) {
        return codeMainDao.get(id);
    }

    public void insert(KPCodeMainDO kpCodeMainDO) {
        if (kpCodeMainDO != null) {
            codeMainDao.insert(kpCodeMainDO);
        }
    }

    public void update(KPCodeMainDO kpCodeMainDO) {
        if (kpCodeMainDO != null) {
            codeMainDao.update(kpCodeMainDO);
        }
    }

    public void delete(Integer id) {
        codeMainDao.delete(id);
    }

    public void insertBatch(List<KPCodeMainDO> kpCodeMainDOList) {
        if (kpCodeMainDOList != null && kpCodeMainDOList.size() > 0) {
            codeMainDao.insertBatch(kpCodeMainDOList);
        }
    }

    public KPCodeMainDO queryByTaxid(String taxid) {
        return codeMainDao.queryByTaxid(taxid);
    }

}
