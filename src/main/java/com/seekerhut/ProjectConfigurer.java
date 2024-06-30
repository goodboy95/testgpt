package com.seekerhut;

import com.seekerhut.argumentresolver.LoginUserArgumentResolver;
import com.seekerhut.interceptors.LoginInterceptor;
import com.seekerhut.model.config.BaseConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration("ProjectConfig")
@Import(BaseConfig.class)

class ProjectConfigurer implements WebMvcConfigurer {
	@Bean
	public LoginInterceptor getLoginInterceptor() {
		return new LoginInterceptor();
	}

	@Bean
	public LoginUserArgumentResolver getLoginUserArgumentResolver() {
		return new LoginUserArgumentResolver();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/**");
	}

	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(getLoginUserArgumentResolver());
    }
}