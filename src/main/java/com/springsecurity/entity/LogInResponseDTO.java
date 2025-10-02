package com.springsecurity.entity;

public class LogInResponseDTO {

    private String userName ;


    private String jwtToken ;

    public LogInResponseDTO() {
    }

    public LogInResponseDTO(String userName, String jwtToken) {
        this.userName = userName;
        this.jwtToken = jwtToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public String toString() {
        return "LogInResponseDTO{" +
                "userName='" + userName + '\'' +
                ", jwtToken='" + jwtToken + '\'' +
                '}';
    }
}
