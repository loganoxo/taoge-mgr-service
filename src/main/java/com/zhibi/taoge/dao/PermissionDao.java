package com.zhibi.taoge.dao;

import com.zhibi.taoge.entity.console.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by QinHe on 2018-10-21.
 */
public interface PermissionDao extends JpaRepository<Permission, Long> {
    List<Permission> findByIdIn(List<Long> permissionIdList);

    List<Permission> findByTypeAndStatusOrderBySortOrder(Integer type, Integer status);

    List<Permission> findByTitle(String title);

    List<Permission> findByParentIdOrderBySortOrder(String parentId);

    List<Permission> findByLevelOrderBySortOrder(Integer level);
}
