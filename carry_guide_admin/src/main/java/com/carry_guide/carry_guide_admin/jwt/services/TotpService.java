package com.carry_guide.carry_guide_admin.jwt.services;

import com.carry_guide.carry_guide_admin.jwt.services.domain.TotpDomain;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Service;

@Service
public class TotpService implements TotpDomain {
    private final GoogleAuthenticator gAuth;

    public TotpService(GoogleAuthenticator gAuth) {
        this.gAuth = gAuth;
    }

    public TotpService() {
        this.gAuth = new GoogleAuthenticator();
    }

    @Override
    public GoogleAuthenticatorKey generateSecretKey() {
        return gAuth.createCredentials();
    }

    @Override
    public String getQrCodeUrl(GoogleAuthenticatorKey secretKey, String username) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("Secure application", username, secretKey);
    }

    @Override
    public boolean verifyCode(String secretKey, int code) {
        return gAuth.authorize(secretKey, code);
    }
}
