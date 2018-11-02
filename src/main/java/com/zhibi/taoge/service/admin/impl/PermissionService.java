package com.zhibi.taoge.service.admin.impl;

import com.zhibi.taoge.dao.PermissionDao;
import com.zhibi.taoge.dao.RolePermissionDao;
import com.zhibi.taoge.dao.UserRoleDao;
import com.zhibi.taoge.entity.console.Permission;
import com.zhibi.taoge.service.admin.IPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限接口实现
 */
@Slf4j
@Service
@Transactional
public class PermissionService implements IPermissionService {

    private final PermissionDao permissionDao;
    private final UserRoleDao userRoleDao;
    private final RolePermissionDao rolePermissionDao;

    public PermissionService(PermissionDao permissionDao, UserRoleDao userRoleDao, RolePermissionDao rolePermissionDao) {
        this.permissionDao = permissionDao;
        this.userRoleDao = userRoleDao;
        this.rolePermissionDao = rolePermissionDao;
    }

    @Override
    public List<Permission> findByLevelOrderBySortOrder(Integer level) {
        return permissionDao.findByLevelOrderBySortOrder(level);
    }

    @Override
    public List<Permission> findByParentIdOrderBySortOrder(String parentId) {
        return permissionDao.findByParentIdOrderBySortOrder(parentId);
    }

    @Override
    public List<Permission> findByTypeAndStatusOrderBySortOrder(Integer type, Integer status) {
        return permissionDao.findByTypeAndStatusOrderBySortOrder(type, status);
    }

    @Override
    public List<Permission> findByTitle(String title) {
        return permissionDao.findByTitle(title);
    }

    @Override
    public List<Permission> findPermissionsByUserId(Long userId) {
        List<Long> roleIds = userRoleDao.findRoleIdsByUserId(userId);
        if (roleIds != null && roleIds.size() > 0) {
            List<Long> permissionIds = rolePermissionDao.findPermissionIdsByRoleIdIn(roleIds);
            if (permissionIds != null && permissionIds.size() > 0) {
                List<Permission> permissions = permissionDao.findByIdIn(permissionIds);
                return permissions;
            }
        }
        return null;
    }

    @Override
    public List<Permission> findPermissionsByRoleId(Long roleId) {
        List<Long> permissionIds = rolePermissionDao.findPermissionIdsByRoleId(roleId);
        if (permissionIds != null && permissionIds.size() > 0) {
            List<Permission> permissions = permissionDao.findByIdIn(permissionIds);
            return permissions;
        }
        return null;
    }

    @Override
    public List<Permission> findPermissionsByTypeAndStatusOrderBySortOrder(Integer type, Integer status) {
        return permissionDao.findByTypeAndStatusOrderBySortOrder(type, status);
    }

}