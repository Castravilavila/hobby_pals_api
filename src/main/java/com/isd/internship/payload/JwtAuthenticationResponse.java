package com.isd.internship.payload;

public class JwtAuthenticationResponse {

    private String accessToken;

    private String tokeType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokeType() {
        return tokeType;
    }

    public void setTokeType(String tokeType) {
        this.tokeType = tokeType;
    }
}