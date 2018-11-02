package com.zhibi.taoge.controller.admin;

import com.zhibi.taoge.common.vo.Result;
import com.zhibi.taoge.service.admin.IPermissionService;
import com.zhibi.taoge.common.utils.ResponseUtil;
import com.zhibi.taoge.constant.CommonConstant;
import com.zhibi.taoge.entity.console.Permission;
import com.zhibi.taoge.service.admin.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(description = "菜单/权限管理接口")
@RequestMapping("/console/admin/permission/")
public class PermissionController {

    private final IUserService userService;
    private final IPermissionService permissionService;

    public PermissionController(IUserService userService, IPermissionService permissionService) {
        this.userService = userService;
        this.permissionService = permissionService;
    }


    @RequestMapping(value = "/getMenuListByUserId/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户页面菜单数据")
    public Result<List<Permission>> getMenuListByUserId(@PathVariable Long userId) {

        //用户所有权限 已排序去重
        List<Permission> list = permissionService.findPermissionsByUserId(userId);
        List<Permission> menuList = new ArrayList<>();
        List<Permission> secondMenuList = new ArrayList<>();
        List<Permission> buttonPermissions = new ArrayList<>();

        if (list != null && list.size() > 0) {
            for (Permission p : list) {
                //筛选一级页面
                if (CommonConstant.PERMISSION_PAGE.equals(p.getType()) && CommonConstant.LEVEL_ONE.equals(p.getLevel())) {
                    menuList.add(p);
                }
                //筛选二级页面
                if (CommonConstant.PERMISSION_PAGE.equals(p.getType()) && CommonConstant.LEVEL_TWO.equals(p.getLevel())) {
                    secondMenuList.add(p);
                }
                //筛选二级页面拥有的按钮权限
                if (CommonConstant.PERMISSION_OPERATION.equals(p.getType()) && CommonConstant.LEVEL_THREE.equals(p.getLevel())) {
                    buttonPermissions.add(p);
                }
            }

            //匹配二级页面拥有权限
            if (secondMenuList != null && secondMenuList.size() > 0) {
                for (Permission p : secondMenuList) {
                    List<String> permTypes = new ArrayList<>();
                    if (buttonPermissions != null && buttonPermissions.size() > 0) {
                        for (Permission pe : buttonPermissions) {
                            if (p.getId().equals(pe.getParentId())) {
                                permTypes.add(pe.getButtonType());
                            }
                        }
                    }
                    p.setPermTypes(permTypes);
                }
            }

            if (menuList != null && menuList.size() > 0) {
                //匹配一级页面拥有二级页面
                for (Permission p : menuList) {
                    List<Permission> secondMenu = new ArrayList<>();
                    if (secondMenuList != null && secondMenuList.size() > 0) {
                        for (Permission pe : secondMenuList) {
                            if (p.getId().equals(pe.getParentId())) {
                                secondMenu.add(pe);
                            }
                        }
                    }
                    p.setChildren(secondMenu);
                }
            }
        }
        return ResponseUtil.createResult(true, 200, "", menuList);
    }

//    @RequestMapping(value = "/getAllList", method = RequestMethod.GET)
//    @ApiOperation(value = "获取权限菜单树")
//    @Cacheable(key = "'allList'")
//    public Result<List<Permission>> getAllList() {
//
//        //一级
//        List<Permission> list = permissionService.findByLevelOrderBySortOrder(CommonConstant.LEVEL_ONE);
//        //二级
//        for (Permission p1 : list) {
//            List<Permission> children1 = permissionService.findByParentIdOrderBySortOrder(p1.getId());
//            p1.setChildren(children1);
//            //三级
//            for (Permission p2 : children1) {
//                List<Permission> children2 = permissionService.findByParentIdOrderBySortOrder(p2.getId());
//                p2.setChildren(children2);
//            }
//        }
//        return new ResultUtil<List<Permission>>().setData(list);
//    }
//
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation(value = "添加")
//    @CacheEvict(key = "'menuList'")
//    public Result<Permission> add(@ModelAttribute Permission permission) {
//
//        // 判断拦截请求的操作权限按钮名是否已存在
//        if (CommonConstant.PERMISSION_OPERATION.equals(permission.getType())) {
//            List<Permission> list = permissionService.findByTitle(permission.getTitle());
//            if (list != null && list.size() > 0) {
//                return new ResultUtil<Permission>().setErrorMsg("名称已存在");
//            }
//        }
//        Permission u = permissionService.save(permission);
//        //重新加载权限
//        mySecurityMetadataSource.loadResourceDefine();
//        //手动删除缓存
//        redisTemplate.delete("permission::allList");
//        return new ResultUtil<Permission>().setData(u);
//    }
//
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation(value = "编辑")
//    public Result<Permission> edit(@ModelAttribute Permission permission) {
//
//        // 判断拦截请求的操作权限按钮名是否已存在
//        if (CommonConstant.PERMISSION_OPERATION.equals(permission.getType())) {
//            // 若名称修改
//            Permission p = permissionService.get(permission.getId());
//            if (!p.getTitle().equals(permission.getTitle())) {
//                List<Permission> list = permissionService.findByTitle(permission.getTitle());
//                if (list != null && list.size() > 0) {
//                    return new ResultUtil<Permission>().setErrorMsg("名称已存在");
//                }
//            }
//        }
//        Permission u = permissionService.update(permission);
//        //重新加载权限
//        mySecurityMetadataSource.loadResourceDefine();
//        //手动批量删除缓存
//        Set<String> keys = redisTemplate.keys("userPermission:" + "*");
//        redisTemplate.delete(keys);
//        Set<String> keysUser = redisTemplate.keys("user:" + "*");
//        redisTemplate.delete(keysUser);
//        Set<String> keysUserMenu = redisTemplate.keys("permission::userMenuList:*");
//        redisTemplate.delete(keysUserMenu);
//        redisTemplate.delete("permission::allList");
//        return new ResultUtil<Permission>().setData(u);
//    }
//
//    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
//    @ApiOperation(value = "批量通过id删除")
//    @CacheEvict(key = "'menuList'")
//    public Result<Object> delByIds(@PathVariable String[] ids) {
//
//        for (String id : ids) {
//            List<RolePermission> list = rolePermissionService.findByPermissionId(id);
//            if (list != null && list.size() > 0) {
//                return new ResultUtil<Object>().setErrorMsg("删除失败，包含正被角色使用关联的菜单或权限");
//            }
//        }
//        for (String id : ids) {
//            permissionService.delete(id);
//        }
//        //重新加载权限
//        mySecurityMetadataSource.loadResourceDefine();
//        //手动删除缓存
//        redisTemplate.delete("permission::allList");
//        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
//    }
}
