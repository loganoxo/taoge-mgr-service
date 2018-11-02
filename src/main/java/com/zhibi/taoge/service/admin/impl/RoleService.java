package com.zhibi.taoge.service.admin.impl;

import com.zhibi.taoge.dao.RoleDao;
import com.zhibi.taoge.dao.UserRoleDao;
import com.zhibi.taoge.entity.console.Role;
import com.zhibi.taoge.entity.console.UserRole;
import com.zhibi.taoge.service.admin.IRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by QinHe on 2018-10-23.
 */
@Service
public class RoleService implements IRoleService {

    private final RoleDao roleDao;
    private final UserRoleDao userRoleDao;

    public RoleService(RoleDao roleDao, UserRoleDao userRoleDao) {
        this.roleDao = roleDao;
        this.userRoleDao = userRoleDao;
    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        List<Long> roleIds = userRoleDao.findRoleIdsByUserId(userId);
        if (roleIds != null && roleIds.size() > 0) {
            List<Role> roles = roleDao.findByIdIn(roleIds);
            return roles;
        }
        return null;
    }

    @Override
    public List<Role> getAll() {
        return roleDao.findAll();
    }

    @Override
    public void saveUserRole(UserRole userRole) {
        userRoleDao.save(userRole);
    }
}
