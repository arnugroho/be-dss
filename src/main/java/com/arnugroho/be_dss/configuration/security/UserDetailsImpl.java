package com.arnugroho.be_dss.configuration.security;

import com.arnugroho.be_dss.model.entity.UserEntity;
import com.arnugroho.be_dss.model.entity.UserProfileEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;
    @Getter
    private JsonNode menu;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username //, String email
            , String password,
                           Collection<? extends GrantedAuthority> authorities, JsonNode menu
    ) {
        this.id = id;
        this.username = username;
//        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.menu = menu;
    }

    public static UserDetailsImpl build(UserEntity user, List<UserProfileEntity> userProfiles) {
        List<GrantedAuthority> authorities = userProfiles.stream()
                .map(userProfile -> new SimpleGrantedAuthority(userProfile.getProfile().getName()))
                .collect(Collectors.toList());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        JsonNode menu = objectMapper.createObjectNode();
//        JsonNode finalMenu = menu;
//        userProfiles.forEach(userProfileEntity -> {
//            try {
//
//                objectMapper.readerForUpdating(finalMenu).readValue(userProfileEntity.getProfile().getAttributeSetValue());
//            } catch (IOException e) {
//                throw new CommonException("Load Menu Gagal");
//            }
//        });
//        menu = userProfiles.get(0).getProfile().getAttributeSetValue();
        ArrayNode menu = objectMapper.createArrayNode();
        for (UserProfileEntity userProfile :userProfiles) {
//            if(userProfile.getProfile().getAttributeSetValue().isArray()){
                menu.addAll((ArrayNode) userProfile.getProfile().getAttributeSetValue());
//            }
        }

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
//                user.getEmail(),
                user.getPassword(),
                authorities, menu);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
