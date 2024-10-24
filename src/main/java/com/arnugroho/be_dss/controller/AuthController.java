package com.arnugroho.be_dss.controller;

import com.arnugroho.be_dss.configuration.jwt.JwtUtils;
import com.arnugroho.be_dss.configuration.security.UserDetailsImpl;
import com.arnugroho.be_dss.model.common.DefaultResponse;
import com.arnugroho.be_dss.model.dto.ChangePasswordDto;
import com.arnugroho.be_dss.model.dto.LoginDto;
import com.arnugroho.be_dss.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final private AuthenticationManager authenticationManager;

    final private JwtUtils jwtUtils;
    final private UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public DefaultResponse<String> authenticateUser(@Valid @RequestBody LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return DefaultResponse.ok(jwt);
    }

    @PostMapping("/changepassword")
    public DefaultResponse<String> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {

        userService.changePassword(changePasswordDto);

        return DefaultResponse.ok();
    }

    @PostMapping("/signup")
    public DefaultResponse<String> registerUser(@Valid @RequestBody JsonNode dto) throws JsonProcessingException {

//
//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(signupDto, userDto);
        userService.userSignUp(dto);


//        Set<String> strRoles = signupDto.getRole();
//        strRoles.forEach(role -> {
//
//            RoleEntity roleEntity = roleRepository.findByName(role)
//                    .orElseThrow(() -> new CommonException("Error: Role " + role + " is not found."));
//
//            UsersRolesEntity usersRolesEntity = new UsersRolesEntity();
//            usersRolesEntity.setUserId(user.getId());
//            usersRolesEntity.setRoleId(roleEntity.getId());
//            usersRolesRepository.save(usersRolesEntity);
//
//
//        });


        return DefaultResponse.ok("User registered successfully!");
    }

    @GetMapping("/userinfo")
    public DefaultResponse<UserDetailsImpl> registerUser(Authentication authentication) {
        UserDetailsImpl userDetails;
//        try {
            userDetails = (UserDetailsImpl) authentication.getPrincipal();

//        } catch (Exception e) {
//            ProfileEntity profile = profileRepository.findByName("ROLE_TENANT").orElseThrow(() -> new EntityNotFoundException("Access tidak ditemukan"));

//            userDetails = new UserDetailsImpl(null,null,null, null, profile.getAttributeSetValue());
//        }
       return  DefaultResponse.ok(userDetails);

    }
}
