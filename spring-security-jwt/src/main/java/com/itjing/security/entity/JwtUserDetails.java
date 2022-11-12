package com.itjing.security.entity;

import com.itjing.security.service.IRoleService;
import com.itjing.security.service.IUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lijing
 * @date 2022年06月09日 21:15
 * @description 自定义UserDetails，创建自己的用户类
 */
public class JwtUserDetails implements UserDetails {


    /**
     * 用户实体类
     */
    private UserEntity user;

    /**
     * 角色业务类
     */
    private IRoleService roleService;

    /**
     * 用户业务类
     */
    private IUserService userService;

    /**
     * 资源信息集合
     */
    private List<ResourceEntity> resourceList;

    /**
     * 角色信息集合
     */
    private List<RoleEntity> roleList;

    /**
     * 更多信息 TODO
     */


    public JwtUserDetails(UserEntity user) {
        this.user = user;
    }

    public JwtUserDetails(UserEntity user, IRoleService roleService, IUserService userService) {
        this.user = user;
        this.roleService = roleService;
        this.userService = userService;
    }

    /**
     * 返回用户所拥有的权限
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // 获取角色信息
        roleList = roleService.getRoles(user.getUserName());
        // 获取资源信息
        resourceList = userService.getResourceList(user.getUserName());
        // 获取更多信息，根据实际情况定义，TODO
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // 获取权限信息
        if (!CollectionUtils.isEmpty(resourceList)) {
            resourceList.forEach(resource -> {
                authorities.add(new SimpleGrantedAuthority(resource.getId() + ":" + resource.getName()));
            });
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    /**
     * 账户是否未过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return user.getIsDeleted().equals(0);
    }

    /**
     * 账户是否未锁定
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return user.getIsLocked().equals(0);
    }

    /**
     * 密码是否未过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否激活
     */
    @Override
    public boolean isEnabled() {
        return user.getStatus().equals(0);
    }

    public UserEntity getUser() {
        return user;
    }

    /**
     * 获取用户资源列表
     *
     * @return
     */
    public List<ResourceEntity> getResourceList() {
        return resourceList;
    }

    /**
     * 获取用户角色列表
     *
     * @return
     */
    public List<RoleEntity> getRoleList() {
        return roleList;
    }


}
