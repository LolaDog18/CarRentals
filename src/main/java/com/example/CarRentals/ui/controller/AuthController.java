package com.example.CarRentals.ui.controller;

import com.example.CarRentals.service.UserService;
import com.example.CarRentals.shared.dto.request.AuthenticationRequestDto;
import com.example.CarRentals.shared.dto.request.RegisterRequestDto;
import com.example.CarRentals.shared.dto.response.AuthenticationResponseDto;
import com.example.CarRentals.shared.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth") // http://localhost:8080/api/v1/auth
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody RegisterRequestDto userDetails) {
        return ResponseEntity.ok(userService.register(userDetails));
    }

    @SneakyThrows
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticateUser(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        return ResponseEntity.ok(userService.authenticate(authenticationRequestDto));
    }
}
