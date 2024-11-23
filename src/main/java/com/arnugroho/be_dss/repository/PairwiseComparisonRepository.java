package com.arnugroho.be_dss.repository;

import com.arnugroho.be_dss.model.entity.PairwiseComparisonEntity;
import com.arnugroho.be_dss.repository.common.CommonRepository;

import java.util.List;
import java.util.Optional;

public interface PairwiseComparisonRepository extends CommonRepository<PairwiseComparisonEntity, Long> {
    Optional<PairwiseComparisonEntity> findByCriteria1IdAndCriteria2Id(Long criteria1, Long criteria2);
    List<PairwiseComparisonEntity> findByCriteria1IdInAndCriteria2IdIn(List<Long> criteria1Ids, List<Long> criteria2Ids);
}
