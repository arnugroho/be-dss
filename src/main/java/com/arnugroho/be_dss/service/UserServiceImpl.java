package com.arnugroho.be_dss.service;

import com.arnugroho.be_dss.configuration.CommonException;
import com.arnugroho.be_dss.mapper.UserMapper;
import com.arnugroho.be_dss.model.dto.ChangePasswordDto;
import com.arnugroho.be_dss.model.dto.UserDto;
import com.arnugroho.be_dss.model.entity.UserEntity;
import com.arnugroho.be_dss.repository.UserRepository;
import com.arnugroho.be_dss.service.common.CommonBaseServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class UserServiceImpl extends CommonBaseServiceImpl<UserEntity, Long, UserDto> implements UserService {
    final private PasswordEncoder encoder;
    final private AuthenticationManager authenticationManager;

    final private UserRepository userRepository;
    private final UserMapper mapper;


    public UserServiceImpl(UserRepository repository, UserMapper mapper, PasswordEncoder encoder, AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(repository, mapper);
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }


    @Override
    public void userSignUp(JsonNode dto) throws JsonProcessingException {
//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(dto, userDto);
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        UserDto userDto = mapper.treeToValue(dto, UserDto.class);

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new CommonException("Error: Username is already taken!");
        }


        userDto.setPassword(encoder.encode("12345678"));
        userDto.setCreatedBy(userDto.getUsername());

        save(userDto);
    }

    @Override
    public void update(JsonNode dto) {
        UserDto userDto = findByUuid(dto.get("uuid").asText());
        super.update(userDto);

    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {
        UserEntity user = userRepository.findByUsername(getUserName()).orElseThrow(() -> new CommonException("Gagal Ganti Password"));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(getUserName(), changePasswordDto.getPassword()));

            user.setPassword(encoder.encode(changePasswordDto.getNewPassword()));
            user.setModifiedBy(getUserName());
            user.setModifiedOn(ZonedDateTime.now());
            userRepository.save(user);

        } catch (Exception e) {
            throw new CommonException("Password Salah");
        }

    }

    @Override
    public UserDto findByUsername(String username) {
        UserEntity entity = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Entity dengan username " + username + " tidak ditemukan"));
        return mapper.toDto(entity);
    }
}
