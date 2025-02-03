package com.example.CarRentals.service.impl;

import com.example.CarRentals.io.entity.Role;
import com.example.CarRentals.io.entity.UserEntity;
import com.example.CarRentals.repository.UserRepository;
import com.example.CarRentals.service.UserService;
import com.example.CarRentals.shared.dto.request.AuthenticationRequestDto;
import com.example.CarRentals.shared.dto.request.RegisterRequestDto;
import com.example.CarRentals.shared.dto.request.UpdateUserRequestDto;
import com.example.CarRentals.shared.dto.response.AuthenticationResponseDto;
import com.example.CarRentals.shared.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import security.JwtService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserResponseDto register(RegisterRequestDto userDetails) {
        var user = UserEntity.builder()
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .email(userDetails.getEmail())
                .password(bCryptPasswordEncoder.encode(userDetails.getPassword()))
                .mobileNumber(userDetails.getMobileNumber())
                .age(userDetails.getAge())
                .drivingLicense(userDetails.getDrivingLicense())
                .emailVerificationToken("")
                .emailVerificationStatus(false)
                .address(userDetails.getAddress())
                .roles(List.of(Role.USER))
                .build();

        userRepository.save(user);

        return UserResponseDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto) {
        var user = userRepository.findByEmail(authenticationRequestDto.getEmail());

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDto.getEmail(), authenticationRequestDto.getPassword()));

        // Generate JWT token
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public UserResponseDto updateUser(String userId, UpdateUserRequestDto user) {
        UserEntity foundUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        BeanUtils.copyProperties(user, foundUser);
        UserResponseDto userResponseDto = new UserResponseDto();
        BeanUtils.copyProperties(foundUser, userResponseDto);
        return userResponseDto;
    }
}
