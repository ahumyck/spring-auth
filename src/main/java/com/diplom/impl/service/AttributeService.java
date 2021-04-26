package com.diplom.impl.service;

import com.diplom.impl.model.entity.Attribute;
import com.diplom.impl.repository.AttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    public boolean createAttribute(String attributeName) {
        if (findAttribute(attributeName) == null) {
            attributeRepository.save(new Attribute(attributeName));
            return true;
        }
        return false;
    }

    public Attribute findAttribute(String roleName) {
        return attributeRepository.findByAttributeName(roleName);
    }
}
