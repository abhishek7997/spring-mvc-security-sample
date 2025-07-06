package com.springboot.demo.mvcsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class DemoController {
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/leaders")
    public String leadersPage() {
        return "leaders";
    }

    @GetMapping("/systems")
    public String systemsPage() {
        return "systems";
    }

    @ModelAttribute("date")
    public String date() {
        return ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss z"));
    }
}
