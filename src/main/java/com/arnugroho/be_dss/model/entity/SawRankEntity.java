package com.arnugroho.be_dss.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "saw_rank")
public class SawRankEntity {

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
