package com.cp.projects.messagingsystem.cpmessagingdomain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class AuthResponse {
    private final String token;
    private final OffsetDateTime validUntil;
}
