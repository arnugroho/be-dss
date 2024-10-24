package com.arnugroho.be_dss.model.dto;

import com.arnugroho.be_dss.model.common.CommonDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto extends CommonDto<Long> {
    private String username;
    @JsonIgnore
    private String password;

}
