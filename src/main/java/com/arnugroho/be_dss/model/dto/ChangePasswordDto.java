package com.arnugroho.be_dss.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordDto {
    private String password;
    private String newPassword;

}
