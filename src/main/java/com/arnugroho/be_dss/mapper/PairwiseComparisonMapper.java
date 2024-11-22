package com.arnugroho.be_dss.mapper;

import com.arnugroho.be_dss.mapper.common.CommonMapper;
import com.arnugroho.be_dss.model.dto.PairwiseComparisonDto;
import com.arnugroho.be_dss.model.entity.PairwiseComparisonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PairwiseComparisonMapper extends CommonMapper<PairwiseComparisonEntity, PairwiseComparisonDto> {
}
