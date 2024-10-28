package com.arnugroho.be_dss.mapper;

import com.arnugroho.be_dss.mapper.common.CommonMapper;
import com.arnugroho.be_dss.model.dto.CriteriaDto;
import com.arnugroho.be_dss.model.dto.CriteriaTreeDto;
import com.arnugroho.be_dss.model.entity.CriteriaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CriteriaTreeMapper extends CommonMapper<CriteriaEntity, CriteriaTreeDto> {
}
