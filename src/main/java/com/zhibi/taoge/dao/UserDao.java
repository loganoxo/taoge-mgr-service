package com.zhibi.taoge.dao;

import com.zhibi.taoge.entity.console.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by QinHe on 2018-10-21.
 */
public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByUsername(String userName);
}
