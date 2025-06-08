package com.carry_guide.carry_guide_admin.security.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class UserInfoResponse {
    private Long id;
    private String username;
    private String mailAddress;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private LocalDate credentialsExpiryDate;
    private LocalDate accountExpiryDate;
    private boolean isTwoFactorEnabled;
    private List<String> userTypes;

    public UserInfoResponse(Long id,
                            String username,
                            String mailAddress,
                            boolean accountNonLocked,
                            boolean accountNonExpired,
                            boolean credentialsNonExpired,
                            boolean enabled,
                            LocalDate credentialsExpiryDate,
                            LocalDate accountExpiryDate,
                            boolean isTwoFactorEnabled,
                            List<String> userTypes) {
        this.id = id;
        this.username = username;
        this.mailAddress = mailAddress;
        this.accountNonLocked = accountNonLocked;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.credentialsExpiryDate = credentialsExpiryDate;
        this.accountExpiryDate = accountExpiryDate;
        this.isTwoFactorEnabled = isTwoFactorEnabled;
        this.userTypes = userTypes;
    }
}
