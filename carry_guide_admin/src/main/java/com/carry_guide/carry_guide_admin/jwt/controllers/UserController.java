package com.carry_guide.carry_guide_admin.jwt.controllers;

import com.carry_guide.carry_guide_admin.jwt.models.entity.UserAccount;
import com.carry_guide.carry_guide_admin.jwt.models.entity.UserType;
import com.carry_guide.carry_guide_admin.jwt.models.request.LoginRequest;
import com.carry_guide.carry_guide_admin.jwt.models.request.SignupRequest;
import com.carry_guide.carry_guide_admin.jwt.models.response.LoginResponse;
import com.carry_guide.carry_guide_admin.jwt.models.response.MessageResponse;
import com.carry_guide.carry_guide_admin.jwt.models.response.UserInfoResponse;
import com.carry_guide.carry_guide_admin.jwt.models.state.UserState;
import com.carry_guide.carry_guide_admin.jwt.repositories.UserAccountRepository;
import com.carry_guide.carry_guide_admin.jwt.repositories.UserTypeRepository;
import com.carry_guide.carry_guide_admin.jwt.security.jwt.JwtUtils;
import com.carry_guide.carry_guide_admin.jwt.services.TotpService;
import com.carry_guide.carry_guide_admin.jwt.services.UserAccountDetails;
import com.carry_guide.carry_guide_admin.jwt.services.UserService;
import com.carry_guide.carry_guide_admin.utils.AuthUtil;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    TotpService totpService;

    @PostMapping("/public/sign_in")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                       loginRequest.getUsername(),
                       loginRequest.getPassword()
                    ));
        } catch (AuthenticationException exception) {
            Map<String, Object> badCredentials = new HashMap<>();
            badCredentials.put("message", "Bad credentials");
            badCredentials.put("status", false);
            return new ResponseEntity<Object>(badCredentials, HttpStatus.NOT_FOUND);
        }

        // set the authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserAccountDetails userAccountDetails = (UserAccountDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userAccountDetails);

        // collect a user type from user details
        List<String> roles = userAccountDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // prepare the response body, now including the JWT token directly in the body
        LoginResponse loginResponse = new LoginResponse(
                userAccountDetails.getUsername(),
                roles,
                jwtToken
        );

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/public/sign_up")
    public ResponseEntity<?> signOut(@Valid @RequestBody SignupRequest signupRequest) {
        if (userAccountRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken."));
        }

        if (userAccountRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is already taken."));
        }

        // create new user's account
        UserAccount userAccount = new UserAccount(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
        );

        Set<String> userRoles = signupRequest.getUserType();
        UserType userType;

        if (userRoles == null || userRoles.isEmpty()) {
            userType = userTypeRepository.findByUserState(UserState.USER_STATE)
                    .orElseThrow(() -> new RuntimeException("User type not found."));
        } else {
            String role = userRoles.iterator().next();
            if (role.equals("admin")) {
                userType = userTypeRepository.findByUserState(UserState.ADMIN_STATE)
                        .orElseThrow(() -> new RuntimeException("User type not found."));
            } else {
                userType = userTypeRepository.findByUserState(UserState.USER_STATE)
                        .orElseThrow(() -> new RuntimeException("User type not found."));
            }

            userAccount.setAccountNonLocked(true);
            userAccount.setAccountNonExpired(true);
            userAccount.setCredentialsNonExpired(true);
            userAccount.setEnabled(true);
            userAccount.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
            userAccount.setAccountExpiryDate(LocalDate.now().plusYears(1));
            userAccount.setTwoFactorEnabled(false);
            userAccount.setSignUpMethod("email");
        }
        userAccount.setUserType(userType);
        userAccountRepository.save(userAccount);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/get_user_details")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        UserAccount userAccount = userService.findUserByUsername(userDetails.getUsername());

        List<String> userTypes = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse userInfoResponse = new UserInfoResponse(
                userAccount.getUserId(),
                userAccount.getUsername(),
                userAccount.getEmail(),
                userAccount.isAccountNonLocked(),
                userAccount.isAccountNonExpired(),
                userAccount.isCredentialsNonExpired(),
                userAccount.isEnabled(),
                userAccount.getCredentialsExpiryDate(),
                userAccount.getAccountExpiryDate(),
                userAccount.isTwoFactorEnabled(),
                userTypes
        );

        return ResponseEntity.ok().body(userInfoResponse);
    }

    @GetMapping("/get_username")
    public String getCurrentUsername(@AuthenticationPrincipal UserDetails userDetails) {
        return (userDetails != null) ? userDetails.getUsername() : "";
    }


    @PostMapping("/public/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            userService.generatePasswordResetToken(email);
            return ResponseEntity.ok(new MessageResponse("Password reset email sent."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error sending password reset email."));
        }
    }

    @PostMapping("/public/reset_password")
    public ResponseEntity<?> resetPassword(@RequestParam String token,
                                           @RequestParam String newPassword) {
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok(new MessageResponse("Password reset successful."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/enable_two_factor_auth")
    public ResponseEntity<String> enable2FA() {
        Long userId = authUtil.loggedInUserId();
        GoogleAuthenticatorKey secret = userService.generate2FASecret(userId);
        String qrCodeUrl = totpService.getQrCodeUrl(secret,
                userService.getUserById(userId).getUsername());
        return ResponseEntity.ok(qrCodeUrl);
    }

    @PostMapping("/disable_two_factor_auth")
    public ResponseEntity<String> disable2FA() {
        Long userId = authUtil.loggedInUserId();
        userService.disable2FA(userId);
        return ResponseEntity.ok("Disabled 2FA");
    }

    @PostMapping("/verify_two_factor_auth")
    public ResponseEntity<String> verify2FA(@RequestParam int code) {
        Long userId = authUtil.loggedInUserId();
        boolean isValid = userService.validate2FACode(userId, code);
        if (isValid) {
            userService.enable2FA(userId);
            return ResponseEntity.ok("Two-factor authentication verified");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid two-factor authentication code");
        }
    }

    @GetMapping("/user/two_factor_auth_status")
    public ResponseEntity<?> get2FAStatus() {
        UserAccount userAccount = authUtil.loggedInUser();
        if (userAccount != null) {
            return ResponseEntity.ok().body(
                    Map.of("is2faEnabled",
                            userAccount.isTwoFactorEnabled()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
    }

    @PostMapping("/public/verify_two_factor_auth_login")
    public ResponseEntity<String> verify2FALogin(@RequestParam int code,
                                                 @RequestParam String jwtToken) {
        String username = jwtUtils.getUserNameFromJwtToken(jwtToken);
        UserAccount userAccount = userService.findUserByUsername(username);
        boolean isValid = userService.validate2FACode(userAccount.getUserId(), code);
        if (isValid) {
            return ResponseEntity.ok("Two-factor authentication verified");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid two-factor authentication code");
        }
    }
}
