package com.example.doancuoiki.controller.user;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình phục vụ ảnh người dùng từ thư mục uploadIMG
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///E://upload/");
        
        // Đảm bảo phục vụ các tài nguyên tĩnh khác (CSS, JS, hình ảnh layout) từ thư mục static
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/"); // Hoặc "classpath:/public/" tùy vào cấu trúc dự án của bạn
    }
}
