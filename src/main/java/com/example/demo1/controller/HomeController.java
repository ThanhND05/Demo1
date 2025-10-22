package com.example.demo1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String showHomePage() {
        return "home/home";
    }

    @GetMapping("/")
    public String showHomePageAlias() {
        return "home/home";
    }
}