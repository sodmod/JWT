package com.Security.JWT.Payload.Request;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
