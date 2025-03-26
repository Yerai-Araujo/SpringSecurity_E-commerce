package com.tfg.wapp.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tfg.wapp.services.AppUserDetailsService;
import com.tfg.wapp.services.KeyLoader;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private String jwtSecret;
	private final AppUserDetailsService appUserDetailsService;
	
	//@Autowired
	public JwtAuthorizationFilter(KeyLoader keyLoader, AppUserDetailsService appUserDetailsService) throws java.io.IOException {
		jwtSecret = keyLoader.loadKey();
		this.appUserDetailsService = appUserDetailsService;
	}


    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException, java.io.IOException {
    	
    	
    	String token = null;
        
        // Check cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // Validate token
        if (token != null) {
            try {
                Jws<Claims> jws = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token);

                Claims claims = jws.getPayload();
                
                if(!claims.getExpiration().before(new Date())) {
	                String username = claims.getSubject();
	                UsernamePasswordAuthenticationToken authenticationToken;
	                
	                if(username.equals("testtfg1"))
	                {
	                	Set<GrantedAuthority> authorities = new HashSet<>();
	                    authorities.add(new SimpleGrantedAuthority("ROLE_PREMIUM"));
	                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	                	authenticationToken = 
			                    new UsernamePasswordAuthenticationToken(username, null, authorities);
	                }
	                else {
		                UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
		                authenticationToken = 
		                    new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
	                }
	                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                else {
                	// Remove the JWT cookie
	                Cookie cookie = new Cookie("JWT", null);
	                cookie.setPath("/");
	                cookie.setMaxAge(0);
	                response.addCookie(cookie);
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
            
        }
        

        
        filterChain.doFilter(request, response);
    }
    
}

