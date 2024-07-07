package com.seekerhut.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    // 通过构造函数注入AuthenticationInterceptor
    @Autowired
    public WebMvcConfig(LoginInterceptor authenticationInterceptor) {
        this.loginInterceptor = authenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**") // 指定拦截的URL模式
                .excludePathPatterns("/api/login/**"); // 指定不拦截的URL模式
    }
}
