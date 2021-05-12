package com.auth.framework.core.adapter;

import com.auth.framework.core.attribute.AttributeConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.PostConstruct;

@Slf4j
public abstract class WebSecurityConfigurableAdapter extends WebSecurityConfigurerAdapter {

    private AttributeConfigurer attributeConfigurer;

    protected void configure(AttributeConfigurer attributeConfigurer) {
        log.debug("Using default configure(AttributeConfigurer). "
                + "If subclassed this will potentially override subclass configure(AttributeConfigurer).");
    }

    @PostConstruct
    protected void postConstruct() {
        configure(attributeConfigurer);
    }

    @Autowired
    public void setAttributeConfigurer(AttributeConfigurer attributeConfigurer) {
        this.attributeConfigurer = attributeConfigurer;
    }

}
