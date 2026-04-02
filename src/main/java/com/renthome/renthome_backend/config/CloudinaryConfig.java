package com.renthome.renthome_backend.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "xxxxx");
        config.put("api_key", "xxxxx");
        config.put("api_secret", "xxxxx");
        return new Cloudinary(config);
    }
}
