package com.arnugroho.be_dss.configuration.security;

import com.arnugroho.be_dss.configuration.CommonException;
import com.arnugroho.be_dss.model.entity.UserEntity;
import com.arnugroho.be_dss.model.entity.UserProfileEntity;
import com.arnugroho.be_dss.repository.UserRepository;
import com.arnugroho.be_dss.repository.UsersProfileRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  final private UserRepository userRepository;
  final private UsersProfileRepository usersProfileRepository;

  public UserDetailsServiceImpl(UserRepository userRepository, UsersProfileRepository usersProfileRepository) {
    this.userRepository = userRepository;
    this.usersProfileRepository = usersProfileRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) {
    UserEntity user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CommonException("User Not Found with username: " + username));
//    List<RoleEntity> roles = usersRolesRepository.findAllByUserId(user.getId()).stream().map(UsersRolesEntity::getRole).collect(Collectors.toList());
    List<UserProfileEntity> userProfiles = usersProfileRepository.findAllByUserId(user.getId());
    return UserDetailsImpl.build(user, userProfiles);
  }

}
