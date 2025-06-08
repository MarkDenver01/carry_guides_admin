package com.carry_guide.carry_guide_admin.jwt.services.domain;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public interface TotpDomain {
    GoogleAuthenticatorKey generateSecretKey();

    String getQrCodeUrl(GoogleAuthenticatorKey secretKey, String username);

    boolean verifyCode(String secretKey, int code);
}
