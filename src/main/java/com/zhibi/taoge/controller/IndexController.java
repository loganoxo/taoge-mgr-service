package com.zhibi.taoge.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by QinHe on 2018-10-20.
 */

@RestController
@RequestMapping("/console/page/")
@Slf4j
public class IndexController {

    @GetMapping("index")
    @PreAuthorize("hasPermission(#a,'aa')")
    public String index(String a) {
        log.info("sd");
        return "index";
    }

}
