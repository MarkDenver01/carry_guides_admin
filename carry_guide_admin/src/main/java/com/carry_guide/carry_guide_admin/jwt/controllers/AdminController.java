package com.carry_guide.carry_guide_admin.jwt.controllers;

import com.carry_guide.carry_guide_admin.jwt.models.entity.UserAccount;
import com.carry_guide.carry_guide_admin.jwt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    UserService userService;

    @GetMapping("/getusers")
    public ResponseEntity<List<UserAccount>> getAllUsers() {
        return new ResponseEntity<>(userService.getUserAccounts(), HttpStatus.OK);
    }
}
