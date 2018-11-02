package com.zhibi.taoge.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by QinHe on 2018-10-21.
 */

@Slf4j
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    //普通的targetDomainObject判断
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        log.error("-------------------------------------");
        return false;
    }

    //用于ACL的访问控制
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        //废弃
        log.error("-------------------------------------");
        return false;
    }
}