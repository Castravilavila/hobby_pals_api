package com.isd.internship.payload;


public class LoginRequest {

    private String password;

    private String usernameOrEmail;

    public LoginRequest(String usernameOrEmail,String password) {
        this.password = password;
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
}
