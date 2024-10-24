package com.arnugroho.be_dss.mapper.common;

import com.arnugroho.be_dss.model.common.SelectDto;

import java.util.List;

public interface CommonDropdownDtoMapper<T>{
    SelectDto toDropdownDto(T param);
    List<SelectDto> toDropdownDtoList(List<T> paramList);
}
