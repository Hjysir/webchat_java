package com.project.webchat_java.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.jdbc.Null;
import org.springframework.expression.spel.ast.NullLiteral;

@Getter
@Setter
public class RequestDto {
    private int code;
    private String message;
    private Object data;

    public RequestDto success() {
        this.code = 200;
        this.message = "success";
        this.data = null;
        return this;
    }

    public RequestDto success(Object data) {
        this.code = 200;
        this.message = "success";
        this.data = data;
        return this;
    }

    public RequestDto fail() {
        this.code = 500;
        this.message = "fail";
        this.data = null;
        return this;
    }

    public RequestDto fail(String message) {
        this.code = 500;
        this.message = message;
        this.data = null;
        return this;
    }

    public RequestDto fail(Object data) {
        this.code = 500;
        this.message = "fail";
        this.data = data;
        return this;
    }

    public RequestDto fail(String message, Object data) {
        this.code = 500;
        this.message = message;
        this.data = data;
        return this;
    }

    public RequestDto fail(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
        return this;
    }

    public RequestDto fail(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        return this;
    }

    public RequestDto fail(int code, Object data) {
        this.code = code;
        this.message = "fail";
        this.data = data;
        return this;
    }

    public RequestDto fail(int code) {
        this.code = code;
        this.message = "fail";
        this.data = null;
        return this;
    }

    public RequestDto fail(Object data, String message) {
        this.code = 500;
        this.message = message;
        this.data = data;
        return this;
    }

    public RequestDto fail(Object data, int code) {
        this.code = code;
        this.message = "fail";
        this.data = data;
        return this;
    }
}
