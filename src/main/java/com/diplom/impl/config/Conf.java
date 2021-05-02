package com.diplom.impl.config;

import com.auth.framework.core.attribute.AttributeConfigurer;
import com.auth.framework.core.attribute.interceptor.AttributeHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Conf implements WebMvcConfigurer {


    @Autowired
    private AttributeConfigurer configurer;

    @Bean
    public AttributeHandlerInterceptor attributeHandlerInterceptor() {
        return new AttributeHandlerInterceptor(configurer);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(attributeHandlerInterceptor());
    }
}
