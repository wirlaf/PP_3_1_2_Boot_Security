package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InitController {

    @GetMapping(value = "/")
    public String init() {
        return "/index";
//        return "redirect:/users";
    }
}
