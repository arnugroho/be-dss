package com.arnugroho.be_dss.repository;

import com.arnugroho.be_dss.model.entity.PredictiveEntity;
import com.arnugroho.be_dss.repository.common.CommonRepository;

import java.util.Optional;

public interface PredictiveRepository extends CommonRepository<PredictiveEntity, Long> {
    Optional<PredictiveEntity> findByAlternativeId(Long alternativeId);
}
