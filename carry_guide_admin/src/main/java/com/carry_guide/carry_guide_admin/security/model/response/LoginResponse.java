package com.carry_guide.carry_guide_admin.security.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LoginResponse {
    private String jwtToken;
    private String username;
    private List<String> userTypes;

    public LoginResponse(String username, List<String> userTypes, String jwtToken) {
        this.username = username;
        this.userTypes = userTypes;
        this.jwtToken = jwtToken;
    }
}
