package com.zhibi.taoge.service.admin.impl;

import cn.hutool.core.date.DateUtil;
import com.zhibi.taoge.common.vo.SearchVo;
import com.zhibi.taoge.dao.*;
import com.zhibi.taoge.entity.console.Permission;
import com.zhibi.taoge.entity.console.Role;
import com.zhibi.taoge.entity.console.User;
import com.zhibi.taoge.service.admin.IPermissionService;
import com.zhibi.taoge.service.admin.IRoleService;
import com.zhibi.taoge.service.admin.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by QinHe on 2018-10-21.
 */

@Service
public class UserService implements IUserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final UserRoleDao userRoleDao;
    private final PermissionDao permissionDao;
    private final RolePermissionDao rolePermissionDao;
    private final IPermissionService permissionService;
    private final IRoleService roleService;

    public UserService(UserDao userDao,
                       RoleDao roleDao,
                       UserRoleDao userRoleDao,
                       PermissionDao permissionDao,
                       RolePermissionDao rolePermissionDao,
                       IPermissionService permissionService,
                       IRoleService roleService) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.userRoleDao = userRoleDao;
        this.permissionDao = permissionDao;
        this.rolePermissionDao = rolePermissionDao;
        this.permissionService = permissionService;
        this.roleService = roleService;
    }

    @Override
    public User findByUsername(String userName) {
        User user = userDao.findByUsername(userName);
        if (user != null) {
            Long userId = user.getId();
            List<Role> roleList = roleService.findRolesByUserId(userId);
            user.setRoles(roleList);
            List<Permission> permissionList = permissionService.findPermissionsByUserId(userId);
            user.setPermissions(permissionList);
        }
        return user;
    }

    @Override
    public Page<User> findByCondition(User user, SearchVo searchVo, Pageable pageable) {
        return userDao.findAll((Specification<User>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            //模糊搜素
            if (StringUtils.isNotBlank(user.getUsername())) {
                list.add(criteriaBuilder.equal(root.get("username"), user.getUsername()));
            }
            //类型
            if (user.getType() != null) {
                list.add(criteriaBuilder.equal(root.get("type"), user.getType()));
            }
            //状态
            if (user.getStatus() != null) {
                list.add(criteriaBuilder.equal(root.get("status"), user.getStatus()));
            }
            //创建时间
            if (StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())) {
                Date start = DateUtil.parse(searchVo.getStartDate());
                Date end = DateUtil.parse(searchVo.getEndDate());
                list.add(criteriaBuilder.between(root.get("createTime").as(Date.class), start, end));
            }
            Predicate[] predicates = new Predicate[list.size()];
            return query.where(list.toArray(predicates)).getRestriction();
        }, pageable);
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }
}
