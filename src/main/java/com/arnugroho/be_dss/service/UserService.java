package com.arnugroho.be_dss.service;

import com.arnugroho.be_dss.model.dto.ChangePasswordDto;
import com.arnugroho.be_dss.model.dto.UserDto;
import com.arnugroho.be_dss.model.entity.UserEntity;
import com.arnugroho.be_dss.service.common.CommonBaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface UserService extends CommonBaseService<UserEntity, Long, UserDto> {
    void userSignUp(JsonNode dto) throws JsonProcessingException;
    void update(JsonNode dto);

    void changePassword(ChangePasswordDto changePasswordDto);

    UserDto findByUsername(String username);
}
