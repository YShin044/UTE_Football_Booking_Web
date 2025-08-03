package com.example.doancuoiki.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Khi trình duyệt yêu cầu file từ URL /uploads/**
        registry.addResourceHandler("/uploads/**")
                // Thì server sẽ tìm file vật lý trong thư mục E:/upload/
                .addResourceLocations("file:/E:/upload/");
    }
}