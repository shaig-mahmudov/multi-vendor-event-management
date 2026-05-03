package org.ironhack.project.eventmanagement.auth.response;

public class AuthResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private long expiresInSeconds;
    private String refreshToken;
    private UserInfoResponse user;

    public AuthResponse() {
    }

    public AuthResponse(String accessToken, String refreshToken, long expiresInSeconds, UserInfoResponse user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresInSeconds = expiresInSeconds;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }

    public void setExpiresInSeconds(long expiresInSeconds) {
        this.expiresInSeconds = expiresInSeconds;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserInfoResponse getUser() {
        return user;
    }

    public void setUser(UserInfoResponse user) {
        this.user = user;
    }
}
