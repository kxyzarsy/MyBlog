package com.myblog.myblog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "Hello, Spring Boot on Port 9090!";
    }
}
