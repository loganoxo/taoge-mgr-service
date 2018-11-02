package com.zhibi.taoge.service.admin;

import com.zhibi.taoge.common.vo.SearchVo;
import com.zhibi.taoge.entity.console.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by QinHe on 2018-10-21.
 */
public interface IUserService {
    User findByUsername(String userName);

    Page<User> findByCondition(User user, SearchVo searchVo, Pageable pageable);

    User save(User user);
}
