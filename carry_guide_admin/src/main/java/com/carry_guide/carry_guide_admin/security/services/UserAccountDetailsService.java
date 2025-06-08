package com.carry_guide.carry_guide_admin.security.services;

import com.carry_guide.carry_guide_admin.jwt.models.entity.UserAccount;
import com.carry_guide.carry_guide_admin.jwt.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAccountDetailsService implements UserDetailsService {
    @Autowired
    UserAccountRepository userAccountRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username:" + username
                ));
        return UserAccountDetails.build(userAccount);
    }
}
