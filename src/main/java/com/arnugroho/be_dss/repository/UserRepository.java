package com.arnugroho.be_dss.repository;

import com.arnugroho.be_dss.model.entity.UserEntity;
import com.arnugroho.be_dss.repository.common.CommonRepository;

import java.util.Optional;

public interface UserRepository extends CommonRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
//    Boolean existsByEmail(String email);

}
