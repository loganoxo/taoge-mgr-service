package com.zhibi.taoge.dao;

import com.zhibi.taoge.entity.console.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by QinHe on 2018-10-21.
 */
public interface RolePermissionDao extends JpaRepository<RolePermission, Long> {
    RolePermission findByIdIn(List<Long> rolePermissionIdList);

    @Query(" select permissionId from  RolePermission where roleId in :roleIdList")
    List<Long> findPermissionIdsByRoleIdIn(@Param("roleIdList") List<Long> roleIdList);

    @Query(" select permissionId from  RolePermission where roleId = :roleId")
    List<Long> findPermissionIdsByRoleId(@Param("roleId") Long roleId);
}
