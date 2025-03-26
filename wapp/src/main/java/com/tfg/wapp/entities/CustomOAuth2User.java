package com.tfg.wapp.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;

@SuppressWarnings("serial")
public class CustomOAuth2User extends DefaultOAuth2User {

	public CustomOAuth2User(OAuth2User oAuth2User, Collection<? extends GrantedAuthority> authorities) {
        super(authorities, oAuth2User.getAttributes(), "login"); // "login" is GitHub's username field
    }
}
