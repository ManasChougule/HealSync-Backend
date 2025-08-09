package com.java.healSync.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Allow CORS for all endpoints
                .allowedOrigins("http://localhost:3000")  // Allow requests from the frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow necessary HTTP methods
                .allowedHeaders("*")  // Allow any headers
                .allowCredentials(true);  // If you need cookies or authorization headers
    }
}
