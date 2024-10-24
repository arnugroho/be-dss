package com.arnugroho.be_dss.mapper;


import com.arnugroho.be_dss.mapper.common.CommonMapper;
import com.arnugroho.be_dss.model.dto.UserDto;
import com.arnugroho.be_dss.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends CommonMapper<UserEntity, UserDto> {
}
