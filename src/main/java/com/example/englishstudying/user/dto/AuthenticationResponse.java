package com.example.englishstudying.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Date;

@Builder(toBuilder = true)
@AllArgsConstructor
public record AuthenticationResponse(String userId, String token, Date issuedAt, Date expiresAt) {
}
