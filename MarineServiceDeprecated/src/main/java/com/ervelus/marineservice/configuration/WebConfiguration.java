package com.ervelus.marineservice.configuration;
import org.springframework.context.annotation.*;

public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }
}
