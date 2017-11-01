package com.aisino.admin.companyCard.codeAssign.service;

import com.aisino.admin.companyCard.codeAssign.bean.KPAssignCodeVO;
import com.aisino.admin.companyCard.codeAssign.bean.KPCodeDetailDO;
import com.aisino.admin.companyCard.codeAssign.bean.KPCodeMainDO;
import com.aisino.admin.global.paging.Page;

import java.util.List;
import java.util.Map;

public interface CodeAssignService {
    List<KPAssignCodeVO> queryPage(Page<KPAssignCodeVO> page);

    int count(Page<KPAssignCodeVO> page);

    List<Map<String, Object>> queryEmployeeList(String taxid);

    boolean assignCodes(KPCodeMainDO kpCodeMainDO,KPCodeDetailDO kpCodeDetailDO) ;
}
