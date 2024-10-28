package com.arnugroho.be_dss.service;

import com.arnugroho.be_dss.model.common.PageableRequest;
import com.arnugroho.be_dss.model.dto.CriteriaDto;
import com.arnugroho.be_dss.model.dto.CriteriaTreeDto;
import com.arnugroho.be_dss.model.entity.CriteriaEntity;
import com.arnugroho.be_dss.service.common.CommonBaseService;
import org.springframework.data.domain.Page;

public interface CriteriaService extends CommonBaseService<CriteriaEntity, Long, CriteriaDto> {
    Page<CriteriaTreeDto> findPagesTree(PageableRequest<CriteriaTreeDto> request);
    Page<CriteriaDto> findPagesChild(PageableRequest<CriteriaDto> request);


}
