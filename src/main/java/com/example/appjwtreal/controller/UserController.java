package com.example.appjwtreal.controller;

import com.example.appjwtreal.payload.ApiResponse;
import com.example.appjwtreal.payload.LoginDto;
import com.example.appjwtreal.payload.RegisterDto;
import com.example.appjwtreal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public HttpEntity<?> registerUser(@RequestBody RegisterDto registerDto){
        ApiResponse apiResponse = userService.registerUserService(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 407).body(apiResponse);
    }

    @GetMapping("/verifyEmail")
    public  HttpEntity<?> verifyEmail(@RequestParam String emailCode, @RequestParam String email){
        ApiResponse apiResponse =  userService.verifyEmailService(emailCode, email);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto){
        ApiResponse apiResponse = userService.loginService(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }



}
