package com.example.CarRentals.ui.controller;

import com.example.CarRentals.service.UserService;
import com.example.CarRentals.shared.dto.request.UpdateUserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(String userId, UpdateUserRequestDto userDetails) {
        try {
            return ResponseEntity.ok(userService.updateUser(userId, userDetails));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
