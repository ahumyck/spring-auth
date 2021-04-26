package com.diplom.impl.repository;

import com.diplom.impl.model.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<Attribute, Integer> {

    Attribute findByAttributeName(String attributeName);
}
