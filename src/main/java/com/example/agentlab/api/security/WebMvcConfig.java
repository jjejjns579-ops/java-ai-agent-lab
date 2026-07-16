package com.example.agentlab.api.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtUserInterceptor jwtUserInterceptor;

    public WebMvcConfig(JwtUserInterceptor jwtUserInterceptor) {
        this.jwtUserInterceptor = jwtUserInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtUserInterceptor)
                .addPathPatterns("/api/**");
    }
}
