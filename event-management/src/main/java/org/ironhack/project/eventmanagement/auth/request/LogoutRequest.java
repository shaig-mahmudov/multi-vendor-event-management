package org.ironhack.project.eventmanagement.auth.request;

import jakarta.validation.constraints.NotBlank;

public class LogoutRequest {
    @NotBlank(message = "Access token is required")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
