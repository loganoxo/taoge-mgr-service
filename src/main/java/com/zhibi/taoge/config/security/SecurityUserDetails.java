package com.zhibi.taoge.config.security;

import com.zhibi.taoge.constant.CommonConstant;
import com.zhibi.taoge.entity.console.Permission;
import com.zhibi.taoge.entity.console.Role;
import com.zhibi.taoge.entity.console.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class SecurityUserDetails extends User implements UserDetails {

    private static final long serialVersionUID = 1L;

    public SecurityUserDetails(User user) {
        if (user != null) {
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());
            this.setStatus(user.getStatus());
            this.setRoles(user.getRoles());
            this.setPermissions(user.getPermissions());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorityList = new ArrayList<>();

        // 添加角色
        List<Role> roles = this.getRoles();
        if (roles != null && roles.size() > 0) {
            roles.forEach(role -> {
                if (StringUtils.isNotBlank(role.getRoleName())) {
                    authorityList.add(new SimpleGrantedAuthority(role.getRoleName()));
                }
            });
        }

        // 添加请求权限
        List<Permission> permissions = this.getPermissions();
        if (permissions != null && permissions.size() > 0) {
            for (Permission permission : permissions) {
                if (CommonConstant.PERMISSION_OPERATION.equals(permission.getType())
                        && StringUtils.isNotBlank(permission.getTitle())
                        && StringUtils.isNotBlank(permission.getPath())) {
                    authorityList.add(new SimpleGrantedAuthority(permission.getTitle()));
                }
            }
        }
        return authorityList;
    }

    /**
     * 账户是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 是否禁用
     */
    @Override
    public boolean isAccountNonLocked() {
        return CommonConstant.USER_STATUS_LOCK.equals(this.getStatus()) ? false : true;
    }

    /**
     * 密码是否过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否启用
     */
    @Override
    public boolean isEnabled() {
        return CommonConstant.USER_STATUS_NORMAL.equals(this.getStatus()) ? true : false;
    }
}