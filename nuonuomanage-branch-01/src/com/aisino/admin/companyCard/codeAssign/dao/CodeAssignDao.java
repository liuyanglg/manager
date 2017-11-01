package com.aisino.admin.companyCard.codeAssign.dao;

import com.aisino.admin.companyCard.codeAssign.bean.KPAssignCodeVO;
import com.aisino.admin.global.paging.Page;

import java.util.List;

public interface CodeAssignDao {
    List<KPAssignCodeVO> queryPage(Page<KPAssignCodeVO> page);

    int count(Page<KPAssignCodeVO> page);
}
