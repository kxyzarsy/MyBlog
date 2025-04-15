package com.myblog.myblog.exception;

import lombok.Getter;

@Getter
public class BlogException extends RuntimeException {
    private final int code; // 自定义错误码

    public BlogException(int code, String message) {
        super(message);
        this.code = code;
    }

}
