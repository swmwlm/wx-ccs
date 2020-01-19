package com.github.wx.ccs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class WxCCSApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WxCCSApplication.class, args);
    }

}
