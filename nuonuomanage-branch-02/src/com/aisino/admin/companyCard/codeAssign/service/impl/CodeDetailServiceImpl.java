package com.aisino.admin.companyCard.codeAssign.service.impl;

import com.aisino.admin.companyCard.codeAssign.bean.KPCodeDetailDO;
import com.aisino.admin.companyCard.codeAssign.dao.CodeDetailDao;
import com.aisino.admin.companyCard.codeAssign.service.CodeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("codeDetailService")
public class CodeDetailServiceImpl implements CodeDetailService{
    @Autowired
    CodeDetailDao codeDetailDao;
    public KPCodeDetailDO get(Integer id) {
        return codeDetailDao.get(id);
    }

    public void insert(KPCodeDetailDO kpCodeDetailDO) {
        if (kpCodeDetailDO != null) {
            codeDetailDao.insert(kpCodeDetailDO);
        }
    }

    public void update(KPCodeDetailDO kpCodeDetailDO) {
        if (kpCodeDetailDO != null) {
            codeDetailDao.update(kpCodeDetailDO);
        }
    }

    public void delete(Integer id) {
        codeDetailDao.delete(id);
    }

    public void insertBatch(List<KPCodeDetailDO> kpCodeDetailDOList) {
        if(kpCodeDetailDOList!=null&&kpCodeDetailDOList.size()>0){
            codeDetailDao.insertBatch(kpCodeDetailDOList);
        }
    }
}
