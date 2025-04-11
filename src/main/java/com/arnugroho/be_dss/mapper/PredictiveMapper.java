package com.arnugroho.be_dss.mapper;

import com.arnugroho.be_dss.mapper.common.CommonMapper;
import com.arnugroho.be_dss.model.dto.PredictiveDto;
import com.arnugroho.be_dss.model.entity.PredictiveEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PredictiveMapper extends CommonMapper<PredictiveEntity, PredictiveDto> {
}
