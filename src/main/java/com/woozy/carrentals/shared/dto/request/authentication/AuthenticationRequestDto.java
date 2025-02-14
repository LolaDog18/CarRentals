package com.woozy.carrentals.shared.dto.request.authentication;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDto {
    private String email;
    private String password;
}
