package com.zhibi.taoge.service.admin;

import com.zhibi.taoge.entity.console.Role;
import com.zhibi.taoge.entity.console.UserRole;

import java.util.List;

/**
 * Created by QinHe on 2018-10-23.
 */
public interface IRoleService {
    List<Role> findRolesByUserId(Long userId);

    List<Role> getAll();

    void saveUserRole(UserRole userRole);
}
