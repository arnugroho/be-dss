package com.arnugroho.be_dss.model.entity;

import com.arnugroho.be_dss.model.common.CommonModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "criteria")
public class CriteriaEntity extends CommonModel {

    @Column(name = "criteria_name", unique = true, nullable = false)
    private String criteriaName;
    @Column(name = "criteria_code", unique = true)
    private String criteriaCode;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "criteria_weight")
    private Double criteriaWeight;
    @Column(name = "criteria_type")
    private String criteriaType;
    @Column(name = "has_child")
    private String hasChild;
    @Column(name = "parent_id")
    private Long criteriaParentId;
    @ManyToOne
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private CriteriaEntity criteriaParent;

    @OneToMany
    @JoinColumn(name = "parent_id") // we need to duplicate the physical information
    @OrderBy("id ASC")
    private Set<CriteriaEntity> children;


}
