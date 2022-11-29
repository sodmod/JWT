package com.Security.JWT.Payload.Request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private Set<String> role;
    @NonNull
    private String password;

}
