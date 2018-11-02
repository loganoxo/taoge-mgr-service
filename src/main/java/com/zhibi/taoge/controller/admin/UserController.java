package com.zhibi.taoge.controller.admin;

import com.zhibi.taoge.common.annotation.SystemLog;
import com.zhibi.taoge.common.utils.PageUtil;
import com.zhibi.taoge.common.utils.ResponseUtil;
import com.zhibi.taoge.common.vo.PageVo;
import com.zhibi.taoge.common.vo.Result;
import com.zhibi.taoge.common.vo.SearchVo;
import com.zhibi.taoge.entity.console.Role;
import com.zhibi.taoge.entity.console.User;
import com.zhibi.taoge.entity.console.UserRole;
import com.zhibi.taoge.service.admin.IRoleService;
import com.zhibi.taoge.service.admin.IUserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by QinHe on 2018-10-23.
 */

@RestController
@RequestMapping("/console/admin/user/")
public class UserController {

    private final IUserService userService;
    private final IRoleService roleService;
    @PersistenceContext
    private EntityManager entityManager;

    public UserController(IUserService userService, IRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @SystemLog(description = "获取当前登录用户接口")
    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    @ApiOperation(value = "获取当前登录用户接口")
    public Result<User> getUserInfo() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User byUsername = userService.findByUsername(user.getUsername());
        // 清除持久上下文环境 避免后面语句导致持久化
        entityManager.clear();
        byUsername.setPassword(null);
        return ResponseUtil.createResult(true, 200, "", byUsername);
    }

    @RequestMapping(value = "getByCondition", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<Page<User>> getByCondition(User user, SearchVo searchVo, PageVo pageVo) {
        Page<User> page = userService.findByCondition(user, searchVo, PageUtil.initPage(pageVo));
        for (User u : page.getContent()) {
            // 关联角色
            List<Role> list = roleService.findRolesByUserId(u.getId());
            u.setRoles(list);
            // 清除持久上下文环境 避免后面语句导致持久化
            entityManager.clear();
            u.setPassword(null);
        }
        return ResponseUtil.createResult(true, 200, "", page);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ApiOperation(value = "添加用户")
    @SystemLog(description = "添加用户")
    public Result<User> add(User userTemp, @RequestParam(required = false) Long[] roles) {
        if (StringUtils.isBlank(userTemp.getUsername()) || StringUtils.isBlank(userTemp.getPassword())) {
            return ResponseUtil.createResult(false, 500, "缺少必需表单字段");
        }

        User byUsername = userService.findByUsername(userTemp.getUsername());
        if (byUsername != null) {
            return ResponseUtil.createResult(false, 500, "该用户名已被注册");
        }

        String encryptPass = new BCryptPasswordEncoder().encode(userTemp.getPassword());
        userTemp.setPassword(encryptPass);
        userTemp.setCreateTime(new Date());
        User user = userService.save(userTemp);
        if (user == null) {
            return ResponseUtil.createResult(false, 500, "添加失败");
        }
        if (roles != null && roles.length > 0) {
            //添加角色
            for (Long roleId : roles) {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                roleService.saveUserRole(userRole);
            }
        }
        return ResponseUtil.createResult(true, 200, "", user);
    }
}
