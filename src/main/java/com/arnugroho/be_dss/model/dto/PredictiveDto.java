package com.arnugroho.be_dss.model.dto;

import com.arnugroho.be_dss.model.common.CommonDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PredictiveDto extends CommonDto<Long> {
    private Float hasil;
    private Long alternativeId;
    private AlternativeDto alternative;


}
