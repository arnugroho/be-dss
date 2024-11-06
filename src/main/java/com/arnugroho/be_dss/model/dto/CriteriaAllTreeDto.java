package com.arnugroho.be_dss.model.dto;

import com.arnugroho.be_dss.model.common.CommonDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class CriteriaAllTreeDto extends CommonDto<Long> {
    private String criteriaName;
    private Double criteriaWeight;
    private String criteriaType;
    private String description;
    private String hasChild;
    private Long criteriaParentId;
    private String criteriaCode;
    private Set<CriteriaAllTreeDto> children;

//    private Set<CriteriaDto> children;



}
