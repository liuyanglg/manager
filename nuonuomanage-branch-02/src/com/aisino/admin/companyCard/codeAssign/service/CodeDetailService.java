package com.aisino.admin.companyCard.codeAssign.service;

import com.aisino.admin.companyCard.codeAssign.bean.KPCodeDetailDO;

import java.util.List;

public interface CodeDetailService {
    KPCodeDetailDO get(Integer id);

    void insert(KPCodeDetailDO kpCodeDetailDO);


    void update(KPCodeDetailDO kpCodeDetailDO);

    void delete(Integer id);

    void insertBatch(List<KPCodeDetailDO> kpCodeDetailDOList);

}
