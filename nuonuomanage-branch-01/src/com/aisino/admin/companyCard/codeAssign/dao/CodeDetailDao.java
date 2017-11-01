package com.aisino.admin.companyCard.codeAssign.dao;

import com.aisino.admin.companyCard.codeAssign.bean.KPCodeDetailDO;

import java.util.List;

public interface CodeDetailDao {
    KPCodeDetailDO get(Integer id);

    void insert(KPCodeDetailDO kpCodeDetailDO);


    void update(KPCodeDetailDO kpCodeDetailDO);

    void delete(Integer id);

    void insertBatch(List<KPCodeDetailDO> kpCodeDetailDOList);

//    void deleteBatch(int[] ids);
}
