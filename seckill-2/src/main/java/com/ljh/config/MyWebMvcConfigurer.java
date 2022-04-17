package com.ljh.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MyWebMvcConfigurer
 *
 * @author ljh
 * created on 2022/4/16 11:53
 */
@Slf4j
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*")
//                .allowedMethods("*")
//                .allowCredentials(true)
//                .maxAge(3600);
//    }
}
