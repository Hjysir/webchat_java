package com.project.webchat_java;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.project.webchat_java.mapper")
public class WebchatJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebchatJavaApplication.class, args);
    }

}
