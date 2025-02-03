package com.example.CarRentals.service;

import com.example.CarRentals.shared.dto.request.AuthenticationRequestDto;
import com.example.CarRentals.shared.dto.request.RegisterRequestDto;
import com.example.CarRentals.shared.dto.request.UpdateUserRequestDto;
import com.example.CarRentals.shared.dto.response.AuthenticationResponseDto;
import com.example.CarRentals.shared.dto.response.UserResponseDto;

public interface UserService {
    UserResponseDto register(RegisterRequestDto userDetails);

    AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto) throws Exception;

    UserResponseDto updateUser(String userId, UpdateUserRequestDto userDetails);
}
