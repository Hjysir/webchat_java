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

    public RequestDto fail(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        return this;
    }
}
