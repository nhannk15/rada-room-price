package com.radar.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    
    @GetMapping("/{id}")
    public String getPathVariable(@PathVariable Long id) {
        return "Find id: " + id;
    }

    @GetMapping("/chat")
    public String getParameter(@RequestParam String message) {
        return "Spring Boot says: " + message;
    }

}
