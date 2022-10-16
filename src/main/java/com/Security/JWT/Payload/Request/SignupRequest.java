package com.Security.JWT.Payload.Request;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private Set<String> role;

    @NotNull
    private String password;

}
