package com.zhibi.taoge.controller.admin;

import com.zhibi.taoge.common.vo.Result;
import com.zhibi.taoge.common.utils.ResponseUtil;
import com.zhibi.taoge.entity.console.Role;
import com.zhibi.taoge.service.admin.IPermissionService;
import com.zhibi.taoge.service.admin.IRoleService;
import com.zhibi.taoge.service.admin.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(description = "菜单/权限管理接口")
@RequestMapping("/console/admin/role/")
public class RoleController {

    private final IUserService userService;
    private final IPermissionService permissionService;
    private final IRoleService roleService;

    public RoleController(IUserService userService, IPermissionService permissionService, IRoleService roleService) {
        this.userService = userService;
        this.permissionService = permissionService;
        this.roleService = roleService;
    }

    @RequestMapping(value = "/getAllList", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部角色")
    public Result<Object> roleGetAll() {
        List<Role> list = roleService.getAll();
        return ResponseUtil.createResult(true, 200, "", list);
    }

}
