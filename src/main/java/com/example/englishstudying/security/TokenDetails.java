package com.example.englishstudying.security;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TokenDetails {
    private String userId;
    private String token;
    private Date issuedAt;
    private Date expiresAt;
}
