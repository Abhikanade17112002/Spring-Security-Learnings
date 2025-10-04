package com.springsecurity.dto;

import java.util.List;

public class AddUserRequestDTO {
    private String firstName ;
    private String lastName ;
    private String emailId ;
    private String password ;
    private List<String> roles ;

    public AddUserRequestDTO() {
    }

    public AddUserRequestDTO(String firstName, String lastName, String emailId, String password, List<String> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.password = password;
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "AddUserRequestDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
