package com.zhibi.taoge.dao;

import com.zhibi.taoge.entity.console.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by QinHe on 2018-10-21.
 */
public interface RoleDao extends JpaRepository<Role, Long> {
    List<Role> findByIdIn(List<Long> roleIdList);
}
