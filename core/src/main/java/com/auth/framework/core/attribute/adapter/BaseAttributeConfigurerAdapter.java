package com.auth.framework.core.attribute.adapter;

import com.auth.framework.core.attribute.AttributeConfigurer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public abstract class BaseAttributeConfigurerAdapter implements AttributeConfigurerAdapter {

    @Autowired
    protected AttributeConfigurer configurer;

    @PostConstruct
    private void init() {
        attributes(configurer);
    }
}
