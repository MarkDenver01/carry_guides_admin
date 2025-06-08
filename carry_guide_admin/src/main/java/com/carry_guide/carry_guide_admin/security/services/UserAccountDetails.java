package com.carry_guide.carry_guide_admin.security.services;

import com.carry_guide.carry_guide_admin.jwt.models.entity.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Data
public class UserAccountDetails implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String mailAddress;
    @JsonIgnore
    private String password;
    private boolean is2faEnabled;
    private Collection<? extends GrantedAuthority> authorities;

    public UserAccountDetails(Long id,
                              String username,
                              String mailAddress,
                              String password,
                              boolean is2faEnabled,
                              Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.mailAddress = mailAddress;
        this.password = password;
        this.is2faEnabled = is2faEnabled;
        this.authorities = authorities;
    }

    public static UserAccountDetails build(UserAccount userAccount) {
        GrantedAuthority authority = new SimpleGrantedAuthority(userAccount.getUserType().getUserState().name());
        return new UserAccountDetails(
                userAccount.getUserId(),
                userAccount.getUserName(),
                userAccount.getMailAddress(),
                userAccount.getPassword(),
                userAccount.isTwoFactorEnabled(),
                List.of(authority)
        );
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public String getPassword() {
        return password;
    }

    public boolean isIs2faEnabled() {
        return is2faEnabled;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccountDetails that = (UserAccountDetails) o;
        return Objects.equals(id, that.id);
    }
}
