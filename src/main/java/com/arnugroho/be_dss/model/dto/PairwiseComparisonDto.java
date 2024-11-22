package com.arnugroho.be_dss.model.dto;

import com.arnugroho.be_dss.model.common.CommonDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PairwiseComparisonDto extends CommonDto<Long> {
    private Long criteria1Id;
    private Long criteria2Id;
    private CriteriaDto criteria1;
    private CriteriaDto criteria2;
    private double score;
}
