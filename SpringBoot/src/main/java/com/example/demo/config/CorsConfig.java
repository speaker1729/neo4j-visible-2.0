package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    private static final long MAX_AGE_SECONDS = 24 * 60 * 60; // 跨域请求最大有效时长（1天）

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("*"); // 允许所有来源访问
        corsConfiguration.addAllowedHeader("*"); // 允许所有请求头
        corsConfiguration.addAllowedMethod("*"); // 允许所有请求方法
        corsConfiguration.setMaxAge(MAX_AGE_SECONDS); // 设置缓存时间

        source.registerCorsConfiguration("/**", corsConfiguration); // 对所有接口路径进行跨域配置

        return new CorsFilter(source); // 创建并返回CorsFilter实例
    }
}
