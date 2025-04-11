package com.arnugroho.be_dss.service;


import com.arnugroho.be_dss.model.dto.PredictiveDto;
import com.arnugroho.be_dss.model.entity.PredictiveEntity;
import com.arnugroho.be_dss.service.common.CommonBaseService;

import java.util.Optional;

public interface PredictiveService extends CommonBaseService<PredictiveEntity, Long, PredictiveDto> {
    Optional<PredictiveEntity> findByAlternativeId(Long alternativeId);
}
