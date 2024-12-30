package com.footballfan.adapter.in.security;

import com.footballfan.application.port.out.UserSaveOutputPort;
import com.footballfan.application.domain.User;
import com.footballfan.application.domain.vo.RoleType;
import com.footballfan.application.port.out.UserFindOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.*;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserFindOutputPort userFindPort;
    private final UserSaveOutputPort userSavePort;
    private final UserDetailsService userDetailsService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        Map<String, Object> userAttributes = oAuth2User.getAttributes();
        String email = (String) userAttributes.get("email");

        User user = userFindPort.findUserByEmail(email).orElseGet(() -> {
            User newUser = User.createUser(email, null, email, RoleType.USER, null, null);
            return userSavePort.saveUser(newUser);
        });
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getId().toString());

        return new DefaultOAuth2User(userDetails.getAuthorities(), userAttributes, "sub");
    }
}
