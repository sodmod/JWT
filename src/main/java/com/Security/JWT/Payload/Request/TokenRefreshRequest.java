package com.Security.JWT.Payload.Request;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class TokenRefreshRequest {
    @NotNull
    private String refreshToken;
}
