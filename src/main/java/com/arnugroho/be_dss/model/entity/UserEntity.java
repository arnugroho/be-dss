package com.arnugroho.be_dss.model.entity;

import com.arnugroho.be_dss.model.common.CommonModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
public class UserEntity extends CommonModel {
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "password", unique = true, nullable = false)
    private String password;
    @Column(name = "contact_id")
    private Long contactId;

}
