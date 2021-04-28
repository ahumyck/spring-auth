package com.diplom.impl.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/attributes")
public class AttributeController {


    @PostMapping(value = "/any")
    public String any() {
        return "I am any";
    }

    @PostMapping(value = "/all")
    public String all() {
        return "I am all";
    }
}
