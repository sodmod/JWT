package com.Security.JWT.Payload.Response;
/*In this class, I updated this class to fit for refresh token response
* I added String refreshtoken to get the refreshtoken and pass it to the user*/
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken,String refreshToken, Long id, String username, String email, List<String> roles){
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
