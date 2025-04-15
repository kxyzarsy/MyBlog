package com.myblog.myblog.exception;

public class NotFoundException extends BlogException {
    public NotFoundException(String message) {
        super(404, message); // 404 表示资源未找到
    }
}