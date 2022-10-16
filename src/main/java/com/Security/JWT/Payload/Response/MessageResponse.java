package com.Security.JWT.Payload.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {
    private String message;
    private int responseCode;

    public MessageResponse(String message, int responseCode){
        this.message = message;
        this.responseCode = responseCode;
    }
}
