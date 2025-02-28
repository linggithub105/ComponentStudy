package com.itjing.activiti.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 用户类
 * @Author: lijing
 * @CreateTime: 2022-09-09 15:11
 */
@Data
public class User implements UserDetails {

    private Integer id;

    private String username;

    private String password;

    private String role;

    // 用户所有权限
    private List<GrantedAuthority> authorities = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
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
}
