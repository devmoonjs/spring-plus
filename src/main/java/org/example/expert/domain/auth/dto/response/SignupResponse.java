package org.example.expert.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SignupResponse {

    private final String bearerToken;
    private final String imgUrl;

    public SignupResponse(String bearerToken, String imgUrl) {
        this.bearerToken = bearerToken;
        this.imgUrl = imgUrl;
    }
}
