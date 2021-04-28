package com.diplom.impl.config;

import com.auth.framework.core.attribute.AttributeConfigurer;
import com.auth.framework.core.attribute.adapter.BaseAttributeConfigurerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AttributeConfig extends BaseAttributeConfigurerAdapter {

    @Override
    public void attributes(AttributeConfigurer attributeConfigurer) {
        attributeConfigurer
                .antMatchesAny("/attributes/any",
                        user -> user.getUsername().equals("user"),
                        user -> user.getPassword().equals("fake"))
                .antMatchesAll("/attributes/all",
                        user -> user.getUsername().equals("user"),
                        user -> user.getPassword().equals("fake"));

    }
}