package com.tfg.wapp.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.tfg.wapp.services.AppUserDetailsService;
import com.tfg.wapp.services.KeyLoader;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	
	private String jwtSecret;
	private long jwtExpirationMs = 86400000;
	
	private final AppUserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	
	//@Autowired
	public CustomUsernamePasswordAuthenticationFilter(AppUserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
			KeyLoader keyLoader) throws IOException {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
		jwtSecret = keyLoader.loadKey();
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/start", "POST"));
    }
	
	@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
			
            // Extract username and password from the request
        	String username = request.getParameter("username");
        	String password = request.getParameter("password");
        	
        	UserDetails user = userDetailsService.loadUserByUsername(username);
        	
        	if(passwordEncoder.matches(password, user.getPassword())) {
        			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, user.getPassword(), user.getAuthorities());
        			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        			return authenticationToken;
        	} else {
        		try {
					unsuccessfulAuthentication(request, response);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		return null;
        	}
    }
	
	@Override
	protected void successfulAuthentication(
	    HttpServletRequest request,
	    HttpServletResponse response,
	    FilterChain chain,
	    Authentication authentication
	) throws IOException {
	    String username = authentication.getName();

	    // Generate the JWT token
	    String token = Jwts.builder()
	        .subject(username)
	        .issuedAt(new Date())
	        .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
	        .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8))) // New key handling
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

	    // Redirect to twoFactorAuthenticator page
	    response.sendRedirect("/qr_code?username=" + username);
          
    }
	
    protected void unsuccessfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        // Log failure
        System.err.println("Authentication failed");
        response.sendRedirect("/start");
    }
}
