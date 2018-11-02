package com.zhibi.taoge.controller;

import com.zhibi.taoge.common.vo.Result;
import com.zhibi.taoge.common.utils.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by QinHe on 2018-10-22.
 */

@Slf4j
@RestController
@RequestMapping("/console/security/")
public class SecurityController {

    @RequestMapping(value = "needLogin", method = RequestMethod.GET)
    @ApiOperation(value = "没有登录")
    public Result<Object> needLogin() {
        return ResponseUtil.createResult(false, 401, "您还未登录");
    }

}

