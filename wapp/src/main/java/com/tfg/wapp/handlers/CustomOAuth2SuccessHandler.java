package com.tfg.wapp.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.tfg.wapp.entities.CustomOAuth2User;
import com.tfg.wapp.services.KeyLoader;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private long jwtExpirationMs = 86400000;
	
	@Autowired
	private KeyLoader keyLoader;
	;
	
	private String loadKey() throws IOException{
		return keyLoader.loadKey();
	}
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        
        String username = oauthUser.getAttribute("login");
        
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_PREMIUM"));  // Default role

        OAuth2User oAuth2User = new CustomOAuth2User(oauthUser, authorities);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(oAuth2User, null, authorities);;

        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", newAuth);
        
        
     // Generate the JWT token
	    String token = Jwts.builder()
	        .subject(username) // Use .subject() instead of .setSubject()
	        .issuedAt(new Date()) // Use .issuedAt() instead of .setIssuedAt()
	        .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
	        .signWith(Keys.hmacShaKeyFor(loadKey().getBytes(StandardCharsets.UTF_8))) // New key handling
	        .compact();

	    response.setContentType("application/json");
	    response.getWriter().write("{\"token\": \"" + token + "\"}");
	    
	    // Set JWT in a cookie
	    Cookie cookie = new Cookie("JWT", token);
	    cookie.setHttpOnly(true);
	    cookie.setSecure(true); // Enable for HTTPS
	    cookie.setPath("/");
	    cookie.setMaxAge((int) (jwtExpirationMs / 1000));
	    response.addCookie(cookie);
        
        response.sendRedirect("/oauth2_Works"); 
    }
}
