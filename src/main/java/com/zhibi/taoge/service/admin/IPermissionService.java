package com.zhibi.taoge.service.admin;

import com.zhibi.taoge.entity.console.Permission;

import java.util.List;

/**
 * 权限接口
 */
public interface IPermissionService {

    /**
     * 通过层级查找 默认升序
     */
    List<Permission> findByLevelOrderBySortOrder(Integer level);

    /**
     * 通过parendId查找
     */
    List<Permission> findByParentIdOrderBySortOrder(String parentId);

    /**
     * 通过类型和状态获取
     */
    List<Permission> findByTypeAndStatusOrderBySortOrder(Integer type, Integer status);

    /**
     * 通过名称获取
     */
    List<Permission> findByTitle(String title);

    List<Permission> findPermissionsByUserId(Long userId);

    List<Permission> findPermissionsByRoleId(Long roleId);

    List<Permission> findPermissionsByTypeAndStatusOrderBySortOrder(Integer type, Integer status);
}