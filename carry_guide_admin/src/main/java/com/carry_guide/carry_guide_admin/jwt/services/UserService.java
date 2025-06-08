package com.carry_guide.carry_guide_admin.jwt.services;

import com.carry_guide.carry_guide_admin.jwt.models.dto.UserDto;
import com.carry_guide.carry_guide_admin.jwt.models.entity.PasswordResetToken;
import com.carry_guide.carry_guide_admin.jwt.models.entity.UserAccount;
import com.carry_guide.carry_guide_admin.jwt.models.entity.UserType;
import com.carry_guide.carry_guide_admin.jwt.models.state.UserState;
import com.carry_guide.carry_guide_admin.jwt.repositories.PasswordResetTokenRepository;
import com.carry_guide.carry_guide_admin.jwt.repositories.UserAccountRepository;
import com.carry_guide.carry_guide_admin.jwt.repositories.UserTypeRepository;
import com.carry_guide.carry_guide_admin.jwt.services.domain.UserDomain;
import com.carry_guide.carry_guide_admin.utils.EmailService;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDomain {
    @Value("${frontend.url")
    String frontEndUrl;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TotpService totpService;

    @Override
    public void updateUserType(Long userId, String userType) {
        UserAccount userAccount = userAccountRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserState userState = UserState.valueOf(userType);
        UserType typeUser = userTypeRepository.findUserTypesBy(userState)
                .orElseThrow(() -> new RuntimeException("User type not found"));
        userAccount.setUserType(typeUser);
        userAccountRepository.save(userAccount);
    }

    @Override
    public List<UserAccount> getUserAccounts() {
        return userAccountRepository.findAll();
    }

    @Override
    public UserDto getUserById(Long userId) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow();
        return convertToDto(userAccount);
    }

    @Override
    public UserAccount findUserByUsername(String username) {
        Optional<UserAccount> userAccount = userAccountRepository.findByUsername(username);
        return userAccount.orElseThrow(()-> new RuntimeException("User not found: " + username));
    }

    @Override
    public void updateUserAccountLockStatus(Long userId, boolean lockStatus) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userAccount.setAccountNonLocked(!lockStatus);
        userAccountRepository.save(userAccount);
    }

    @Override
    public List<UserType> getUserStates() {
        return userTypeRepository.findAll();
    }

    @Override
    public void updateAccountExpiryStatus(Long userId, boolean expiryStatus) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userAccount.setAccountNonExpired(!expiryStatus);
        userAccountRepository.save(userAccount);
    }

    @Override
    public void updateAccountEnabledStatus(Long userId, boolean enabledStatus) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userAccount.setEnabled(enabledStatus);
        userAccountRepository.save(userAccount);

    }

    @Override
    public void updateCredentialsExpiryStatus(Long userId, boolean expiredStatus) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userAccount.setCredentialsNonExpired(!expiredStatus);
        userAccountRepository.save(userAccount);
    }

    @Override
    public void updatePassword(Long userId, String password) {
        try {
            UserAccount userAccount= userAccountRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            userAccount.setPassword(passwordEncoder.encode(password));
            userAccountRepository.save(userAccount);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update password");
        }
    }

    @Override
    public void generatePasswordResetToken(String email) {
        UserAccount userAccount = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);
        PasswordResetToken resetToken = new PasswordResetToken(token,expiryDate, userAccount);
        passwordResetTokenRepository.save(resetToken);

        String resetUrl = frontEndUrl + "/reset-token?token=" + token;
        emailService.sendPasswordResetEmail(userAccount.getEmail(), resetUrl);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isUsed()) throw new RuntimeException("Reset token is already been used");

        if (resetToken.getExpiryDate().isBefore(Instant.now())) throw new RuntimeException("Reset token has expired");

        UserAccount userAccount = resetToken.getUserAccount();
        userAccount.setPassword(passwordEncoder.encode(newPassword));
        userAccountRepository.save(userAccount);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    @Override
    public Optional<UserAccount> findUserByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }

    @Override
    public UserAccount registerUser(UserAccount user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userAccountRepository.save(user);
    }

    @Override
    public GoogleAuthenticatorKey generate2FASecret(Long userId) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        GoogleAuthenticatorKey key = totpService.generateSecretKey();
        userAccount.setTwoFactorSecret(key.getKey());
        userAccountRepository.save(userAccount);
        return key;
    }

    @Override
    public boolean validate2FACode(Long userId, int code) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return totpService.verifyCode(userAccount.getTwoFactorSecret(), code);
    }

    @Override
    public void enable2FA(Long userId) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userAccount.setAccountNonLocked(true);
        userAccountRepository.save(userAccount);
    }

    @Override
    public void disable2FA(Long userId) {
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userAccount.setAccountNonLocked(false);
        userAccountRepository.save(userAccount);
    }

    private UserDto convertToDto(UserAccount userAccount) {
        return new UserDto(
                userAccount.getUserId(),
                userAccount.getUsername(),
                userAccount.getEmail(),
                userAccount.isAccountNonLocked(),
                userAccount.isAccountNonExpired(),
                userAccount.isCredentialsNonExpired(),
                userAccount.isEnabled(),
                userAccount.getCredentialsExpiryDate(),
                userAccount.getAccountExpiryDate(),
                userAccount.getTwoFactorSecret(),
                userAccount.isTwoFactorEnabled(),
                userAccount.getSignUpMethod(),
                userAccount.getUserType(),
                userAccount.getCreatedDate(),
                userAccount.getUpdatedDate()
        );
    }
}
