package com.arnugroho.be_dss.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "wp_rank")
public class WpRankEntity {

    @Id
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "alternative_name")
    private String alternativeName;
    @Column(name = "score")
    private Double score;
    @Column(name = "rank")
    private Integer rank;
}
