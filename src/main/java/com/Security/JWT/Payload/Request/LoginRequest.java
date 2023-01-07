package com.Security.JWT.Payload.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {

    @NonNull
    private String username;

    @NonNull
    private String password;
}
