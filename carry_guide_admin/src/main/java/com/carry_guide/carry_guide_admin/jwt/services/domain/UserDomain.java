package com.carry_guide.carry_guide_admin.jwt.services.domain;

import com.carry_guide.carry_guide_admin.jwt.models.dto.UserDto;
import com.carry_guide.carry_guide_admin.jwt.models.entity.UserAccount;
import com.carry_guide.carry_guide_admin.jwt.models.entity.UserType;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import java.util.List;
import java.util.Optional;

public interface UserDomain {
    void updateUserType(Long userId, String userType);

    List<UserAccount> getUserAccounts();

    UserDto getUserById(Long userId);

    UserAccount findUserByUsername(String username);

    void updateUserAccountLockStatus(Long userId, boolean lockStatus);

    List<UserType> getUserStates();

    void updateAccountExpiryStatus(Long userId, boolean expiryStatus);

    void updateAccountEnabledStatus(Long userId, boolean enabledStatus);

    void updateCredentialsExpiryStatus(Long userId, boolean expiredStatus);

    void updatePassword(Long userId, String password);

    void generatePasswordResetToken(String email);

    void resetPassword(String token, String newPassword);

    Optional<UserAccount> findUserByEmail(String email);

    UserAccount registerUser(UserAccount user);

    GoogleAuthenticatorKey generate2FASecret(Long userId);

    boolean validate2FACode(Long userId, String code);

    void enable2FA(Long userId);

    void disable2FA(Long userId);
}
