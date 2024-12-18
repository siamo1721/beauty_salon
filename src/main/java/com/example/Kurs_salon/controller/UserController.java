//package com.example.Kurs_salon.controller;
//
//import com.example.Kurs_salon.model.User;
//import com.example.Kurs_salon.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//public class UserController {
//    private final UserService userService;
//
//    @PostMapping
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        return ResponseEntity.ok(userService.createUser(user));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUser(@PathVariable Long id) {
//        return ResponseEntity.ok(userService.getUserById(id));
//    }
//}