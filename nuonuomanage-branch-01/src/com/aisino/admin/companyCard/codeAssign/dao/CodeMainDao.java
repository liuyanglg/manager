package com.aisino.admin.companyCard.codeAssign.dao;

import com.aisino.admin.companyCard.codeAssign.bean.KPCodeMainDO;

import java.util.List;

public interface CodeMainDao {
    KPCodeMainDO get(Integer id);

    void insert(KPCodeMainDO kpCodeMainDO);

    void update(KPCodeMainDO kpCodeMainDO);

    void delete(Integer id);

    void insertBatch(List<KPCodeMainDO> kpCodeMainDOList);

    KPCodeMainDO queryByTaxid(String taxid);

}
