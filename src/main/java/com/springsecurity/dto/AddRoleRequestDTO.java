package com.springsecurity.dto;

import java.util.List;

public class AddRoleRequestDTO {

    private String roleName ;

    private List<String> authorityIds ;

    public AddRoleRequestDTO() {
    }

    public AddRoleRequestDTO(String roleName, List<String> authorityIds) {
        this.roleName = roleName;
        this.authorityIds = authorityIds;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<String> getAuthorityIds() {
        return authorityIds;
    }

    public void setAuthorityIds(List<String> authorityIds) {
        this.authorityIds = authorityIds;
    }

    @Override
    public String toString() {
        return "AddRoleRequestDTO{" +
                "roleName='" + roleName + '\'' +
                ", authorityIds=" + authorityIds +
                '}';
    }
}
