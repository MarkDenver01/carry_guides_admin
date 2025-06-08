package com.carry_guide.carry_guide_admin.jwt.services;

import com.carry_guide.carry_guide_admin.jwt.models.dto.UserDto;
import com.carry_guide.carry_guide_admin.jwt.models.entity.UserAccount;
import com.carry_guide.carry_guide_admin.jwt.models.entity.UserType;
import com.carry_guide.carry_guide_admin.jwt.services.domain.UserDomain;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDomain {
    @Value("${frontend.url")
    String frontEndUrl;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void updateUserType(Long userId, String userType) {

    }

    @Override
    public List<UserAccount> getUserAccounts() {
        return List.of();
    }

    @Override
    public UserDto getUserById(Long userId) {
        return null;
    }

    @Override
    public UserAccount findUserByUsername(String username) {
        return null;
    }

    @Override
    public void updateUserAccountLockStatus(Long userId, boolean lockStatus) {

    }

    @Override
    public List<UserType> getUserStates() {
        return List.of();
    }

    @Override
    public void updateAccountExpiryStatus(Long userId, boolean expiryStatus) {

    }

    @Override
    public void updateAccountEnabledStatus(Long userId, boolean enabledStatus) {

    }

    @Override
    public void updateCredentialsExpiryStatus(Long userId, boolean expiredStatus) {

    }

    @Override
    public void updatePassword(Long userId, String password) {

    }

    @Override
    public void generatePasswordResetToken(String email) {

    }

    @Override
    public void resetPassword(String token, String newPassword) {

    }

    @Override
    public Optional<UserAccount> findUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public UserAccount registerUser(UserAccount user) {
        return null;
    }

    @Override
    public GoogleAuthenticatorKey generate2FASecret(Long userId) {
        return null;
    }

    @Override
    public boolean validate2FACode(Long userId, String code) {
        return false;
    }

    @Override
    public void enable2FA(Long userId) {

    }

    @Override
    public void disable2FA(Long userId) {

    }
}
