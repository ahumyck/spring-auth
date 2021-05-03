package com.diplom.impl.config;

import com.auth.framework.core.attribute.AttributeConfigurer;
import com.auth.framework.core.attribute.interceptor.AttributeHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class Conf implements WebMvcConfigurer {

    @Autowired
    private AttributeConfigurer configurer;

    @Bean
    public AttributeHandlerInterceptor attributeHandlerInterceptor() {
        log.info("@Bean attributeHandlerInterceptor");
        return new AttributeHandlerInterceptor(configurer);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("addInterceptors InterceptorRegistry");
        registry.addInterceptor(attributeHandlerInterceptor());
    }
}
