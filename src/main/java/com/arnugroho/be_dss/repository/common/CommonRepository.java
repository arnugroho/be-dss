package com.arnugroho.be_dss.repository.common;

import com.arnugroho.be_dss.model.common.CommonModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface CommonRepository<T extends CommonModel, ID extends Serializable> extends JpaRepository<T, ID>,
        QuerydslPredicateExecutor<T>, JpaSpecificationExecutor<T> {
    Page<T> findAll(Specification<T> specification, Pageable pageable);

//    Optional<T> findByUuid(String uuid);
}
