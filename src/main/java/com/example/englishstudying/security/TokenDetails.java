package com.example.englishstudying.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDetails {
    private long userId;
    private String token;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
}
