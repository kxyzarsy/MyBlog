//登录页控制器
package com.myblog.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/posts")
    public String index() {
        return "index";
    }
}
