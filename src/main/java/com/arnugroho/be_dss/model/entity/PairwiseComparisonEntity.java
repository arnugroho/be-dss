package com.arnugroho.be_dss.model.entity;

import com.arnugroho.be_dss.model.common.CommonModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "pairwise_comparisons")
public class PairwiseComparisonEntity extends CommonModel {

    @ManyToOne
    @JoinColumn(name = "criteria_1", insertable = false, updatable = false)
    private CriteriaEntity criteria1;

    @Column(name = "criteria_1", nullable = false)
    private Long criteria1Id;

    @ManyToOne
    @JoinColumn(name = "criteria_2", insertable = false, updatable = false)
    private CriteriaEntity criteria2;

    @Column(name = "criteria_2", nullable = false)
    private Long criteria2Id;

    @Column(nullable = false)
    private Double score;
}
