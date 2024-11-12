package com.arnugroho.be_dss.repository;

import com.arnugroho.be_dss.model.entity.CriteriaEntity;
import com.arnugroho.be_dss.model.projection.SumWeightProjection;
import com.arnugroho.be_dss.repository.common.CommonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CriteriaRepository extends CommonRepository<CriteriaEntity, Long> {
    Page<CriteriaEntity> findAllByHasChildAndStatusDeleteFalse(String hasChild, Pageable pageable);

    @Query(value = "select ct.parent_id parentId, c.criteria_name parentName, sum(ct.criteria_weight) sumWeight from criteria ct " +
            "left join criteria c on c.id = ct.parent_id " +
            "where ct.status_delete is false " +
            "group by ct.parent_id , c.criteria_name ", nativeQuery = true)
    List<SumWeightProjection> sumWeight();

    @Query(value = "select sum(ct.criteria_weight) from criteria ct " +
            "where ct.has_child = 'TIDAK' and ct.status_delete is false ", nativeQuery = true)
    Double sumWeightAll();
}
