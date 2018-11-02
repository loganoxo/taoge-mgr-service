package com.zhibi.taoge.dao;

import com.zhibi.taoge.entity.console.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by QinHe on 2018-10-21.
 */
public interface UserRoleDao extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUserId(Long userId);

    @Query(" select roleId from UserRole where userId=:userId")
    List<Long> findRoleIdsByUserId(@Param("userId") Long userId);
}
