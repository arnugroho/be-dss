package com.arnugroho.be_dss.model.entity;

import com.arnugroho.be_dss.model.common.CommonModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "predictive")
public class PredictiveEntity extends CommonModel {
    @Column(name = "alternative_id")
    private Long alternativeId;
    @ManyToOne
    @JoinColumn(name = "alternative_id", insertable = false, updatable = false)
    private AlternativeEntity alternative;
    @Column(name = "hasil")
    private Float hasil;
}
