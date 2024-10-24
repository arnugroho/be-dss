package com.arnugroho.be_dss.mapper.common;

import com.arnugroho.be_dss.model.common.CommonModel;
import com.arnugroho.be_dss.model.common.SelectDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface CommonMapper<T extends CommonModel, DTO> {
    DTO toDto(T param);
    T fromDto(DTO param);
    List<T> toEntityList(List<DTO> dtoList);
    List<DTO> toDtoList(List<T> entityList);
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    T toEntityForUpdate(DTO fto, @MappingTarget T entity);
    SelectDto toDropdownEntity(T param);
    SelectDto toDropdownDto(DTO param);
    List<SelectDto> toDropdownDtoList(List<DTO> paramList);

    List<SelectDto> toDropdownEntityList(List<T> paramList);
}
