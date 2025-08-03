package com.example.doancuoiki.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("removal")
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Tùy vào nhu cầu, có thể bật lại
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() // Cho phép truy cập mọi route
            )
            .formLogin(form -> form.disable()); // Vô hiệu hóa login mặc định

        return http.build();
    }
}
