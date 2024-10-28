package com.arnugroho.be_dss.model.dto;

import com.arnugroho.be_dss.model.common.CommonDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AlternativeDto extends CommonDto<Long> {
    private JsonNode dataValue;


}
