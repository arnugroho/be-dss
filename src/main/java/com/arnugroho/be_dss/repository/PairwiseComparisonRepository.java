package com.arnugroho.be_dss.repository;

import com.arnugroho.be_dss.model.entity.PairwiseComparisonEntity;
import com.arnugroho.be_dss.repository.common.CommonRepository;

import java.util.Optional;

public interface PairwiseComparisonRepository extends CommonRepository<PairwiseComparisonEntity, Long> {
    Optional<PairwiseComparisonEntity> findByCriteria1AndCriteria2(Long criteria1, Long criteria2);
}
