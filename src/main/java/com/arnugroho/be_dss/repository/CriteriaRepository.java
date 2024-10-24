package com.arnugroho.be_dss.repository;

import com.arnugroho.be_dss.model.entity.CriteriaEntity;
import com.arnugroho.be_dss.repository.common.CommonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface CriteriaRepository extends CommonRepository<CriteriaEntity, Long> {
    Page<CriteriaEntity> findAllByHasChildAndStatusDeleteFalse(String hasChild, Pageable pageable);
}
