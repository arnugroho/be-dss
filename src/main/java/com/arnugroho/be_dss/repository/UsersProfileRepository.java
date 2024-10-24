package com.arnugroho.be_dss.repository;

import com.arnugroho.be_dss.model.entity.UserProfileEntity;
import com.arnugroho.be_dss.repository.common.CommonRepository;

import java.util.List;

public interface UsersProfileRepository extends CommonRepository<UserProfileEntity, Long> {
    List<UserProfileEntity> findAllByUserId(Long userId);

}
