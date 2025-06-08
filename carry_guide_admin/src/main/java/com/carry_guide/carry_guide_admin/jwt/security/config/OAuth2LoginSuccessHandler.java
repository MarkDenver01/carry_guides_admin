package com.carry_guide.carry_guide_admin.jwt.security.config;

import com.carry_guide.carry_guide_admin.jwt.models.entity.UserAccount;
import com.carry_guide.carry_guide_admin.jwt.models.entity.UserType;
import com.carry_guide.carry_guide_admin.jwt.models.state.UserState;
import com.carry_guide.carry_guide_admin.jwt.repositories.UserTypeRepository;
import com.carry_guide.carry_guide_admin.jwt.services.UserService;
import com.carry_guide.carry_guide_admin.jwt.security.jwt.JwtUtils;
import com.carry_guide.carry_guide_admin.jwt.services.UserAccountDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private final UserService userService;

    @Autowired
    private final JwtUtils jwtUtils;

    @Autowired
    UserTypeRepository userTypeRepository;

    @Value("${frontend.url}")
    private String frontEndUrl;

    String username;
    String idAttributeKey;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        if ("google".equals(authenticationToken.getAuthorizedClientRegistrationId())) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authenticationToken.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String email = attributes.getOrDefault("email", "").toString();
            String name = attributes.getOrDefault("name", "").toString();
            username = email.split("@")[0];
            idAttributeKey = "sub";

            System.out.println("OAUTH: " + email + " : " + name + ": " + username );

            userService.findUserByEmail(email)
                    .ifPresentOrElse(user -> {
                        DefaultOAuth2User oAuth2User = new DefaultOAuth2User(
                                List.of(new SimpleGrantedAuthority(
                                        user.getUserType().getUserState().name()
                                )),
                                attributes,
                                idAttributeKey
                        );

                        Authentication securityAuth = new OAuth2AuthenticationToken(
                                oAuth2User,
                                List.of(new SimpleGrantedAuthority(
                                        user.getUserType().getUserState().name()
                                )),
                                authenticationToken.getAuthorizedClientRegistrationId()
                        );
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    }, ()-> {
                        UserAccount newUser = new UserAccount();
                        Optional<UserType> userState = userTypeRepository.findUserTypesBy(UserState.USER_STATE);
                        if (userState.isPresent()) {
                            newUser.setUserType(userState.get());
                        } else {
                            // Handle the case where the role is not found
                            throw new RuntimeException("Default role not found");
                        }
                        newUser.setEmail(email);
                        newUser.setUsername(username);
                        newUser.setSignUpMethod(authenticationToken.getAuthorizedClientRegistrationId());
                        userService.registerUser(newUser);
                        DefaultOAuth2User oAuth2User = new DefaultOAuth2User(
                                List.of(new SimpleGrantedAuthority(
                                        newUser.getUserType().getUserState().name()
                                )),
                                attributes,
                                idAttributeKey
                        );
                        Authentication securityAuth = new OAuth2AuthenticationToken(
                                oAuth2User,
                                List.of(new SimpleGrantedAuthority(
                                        newUser.getUserType().getUserState().name()
                                )),
                                authenticationToken.getAuthorizedClientRegistrationId()
                        );
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    });
        }
        this.setAlwaysUseDefaultTargetUrl(true);

        // JWT token logic
        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauth2User.getAttributes();

        // extract neccesasry attributes
        String email = attributes.get("email").toString();
        System.out.println("OAuth2LoginSuccessHandler: " + username + " : " + email);

        Set<SimpleGrantedAuthority> authorities = new HashSet<>(
                oauth2User.getAuthorities().stream()
                        .map(authority ->
                                new SimpleGrantedAuthority(authority.getAuthority()))
                        .collect(Collectors.toList()));
        UserAccount userAccount = userService.findUserByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        authorities.add(new SimpleGrantedAuthority(userAccount.getUserType().getUserState().name()));

        // create user account details
        UserAccountDetails userAccountDetails = new UserAccountDetails(
                null,
                username,
                email,
                null,
                false,
                authorities
        );

        // generate jwt token
        String jwtToken = jwtUtils.generateTokenFromUsername(userAccountDetails);

        // redirect to the frontend with jwt token
        String targetUrl = UriComponentsBuilder.fromUriString(frontEndUrl + "/oauth2/redirect")
                .queryParam("token", jwtToken)
                .build().toUriString();
        this.setDefaultTargetUrl(targetUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
