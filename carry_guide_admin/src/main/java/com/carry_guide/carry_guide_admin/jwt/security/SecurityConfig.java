package com.carry_guide.carry_guide_admin.jwt.security;

import com.carry_guide.carry_guide_admin.jwt.models.entity.UserAccount;
import com.carry_guide.carry_guide_admin.jwt.models.entity.UserType;
import com.carry_guide.carry_guide_admin.jwt.models.state.UserState;
import com.carry_guide.carry_guide_admin.jwt.repositories.UserAccountRepository;
import com.carry_guide.carry_guide_admin.jwt.repositories.UserTypeRepository;
import com.carry_guide.carry_guide_admin.jwt.security.config.OAuth2LoginSuccessHandler;
import com.carry_guide.carry_guide_admin.jwt.security.jwt.AuthEntryPointJwt;
import com.carry_guide.carry_guide_admin.jwt.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.time.LocalDate;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    @Lazy
    OAuth2LoginSuccessHandler successHandler;


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/auth/public/**"));

        http.authorizeHttpRequests((request) ->
                request
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/csrf-token").permitAll()
                        .requestMatchers("/api/auth/public/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 ->
                        oauth2.successHandler(successHandler));
        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint(unauthorizedHandler));
        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public CommandLineRunner initData(UserTypeRepository userTypeRepository,
                                      UserAccountRepository userAccountRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            UserType userType = userTypeRepository.findUserTypesBy(UserState.USER_STATE)
                    .orElseGet(() -> userTypeRepository.save(new UserType(UserState.USER_STATE)));

            UserType adminType = userTypeRepository.findUserTypesBy(UserState.ADMIN_STATE)
                    .orElseGet(() -> userTypeRepository.save(new UserType(UserState.ADMIN_STATE)));

            if (!userAccountRepository.existsByUsername("user1")) {
                UserAccount accountUser = new UserAccount(
                        "user1",
                        "user1@example.com",
                        passwordEncoder.encode("password1")
                );
                accountUser.setAccountNonLocked(false);
                accountUser.setAccountNonExpired(true);
                accountUser.setCredentialsNonExpired(true);
                accountUser.setEnabled(true);
                accountUser.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                accountUser.setAccountExpiryDate(LocalDate.now().plusYears(1));
                accountUser.setTwoFactorEnabled(false);
                accountUser.setSignUpMethod("email");
                accountUser.setUserType(userType);
                userAccountRepository.save(accountUser);
            }

            if (!userAccountRepository.existsByUsername("admin")) {
                UserAccount accountAdmin = new UserAccount(
                        "admin",
                        "admin@example.com",
                        passwordEncoder.encode("adminPass")
                );
                accountAdmin.setAccountNonLocked(false);
                accountAdmin.setAccountNonExpired(true);
                accountAdmin.setCredentialsNonExpired(true);
                accountAdmin.setEnabled(true);
                accountAdmin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                accountAdmin.setAccountExpiryDate(LocalDate.now().plusYears(1));
                accountAdmin.setTwoFactorEnabled(false);
                accountAdmin.setSignUpMethod("email");
                accountAdmin.setUserType(adminType);
                userAccountRepository.save(accountAdmin);
            }
        };
    }
}
