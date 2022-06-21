package com.example.appjwtreal.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {
    @GetMapping("/getHome")
    public HttpEntity<?> getHome(){

        return ResponseEntity.ok("Welcome to Home page");
    }

}
