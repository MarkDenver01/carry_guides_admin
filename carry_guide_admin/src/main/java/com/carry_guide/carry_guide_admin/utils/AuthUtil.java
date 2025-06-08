package com.carry_guide.carry_guide_admin.utils;

import com.carry_guide.carry_guide_admin.jwt.models.entity.UserAccount;
import com.carry_guide.carry_guide_admin.jwt.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    UserAccountRepository userAccountRepository;

    public Long loggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = userAccountRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new RuntimeException("User Not Found"));
        return userAccount.getUserId();
    }

    public UserAccount loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userAccountRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new RuntimeException("User Not Found"));
    }
}
