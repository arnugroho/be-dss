package com.arnugroho.be_dss.model.dto;

import com.arnugroho.be_dss.model.common.CommonDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class CriteriaTreeDto extends CommonDto<Long> {
    private String criteriaName;
    private Double criteriaWeight;
    private String criteriaType;
    private String description;
    private String hasChild;
//    private CriteriaDto criteriaParent;
    private Long criteriaParentId;
    private Set<CriteriaTreeDto> children;



}
