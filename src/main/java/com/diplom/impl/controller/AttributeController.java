package com.diplom.impl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/attributes")
public class AttributeController {


    @PostMapping(value = "/any")
    public String postAny() {
        return "I am any";
    }

    @PostMapping(value = "/all")
    public String postAll() {
        return "I am all";
    }

    @GetMapping(value = "/any")
    public String getAny() {
        return "I am any";
    }

    @GetMapping(value = "/all")
    public String getAll() {
        return "I am all";
    }
}
