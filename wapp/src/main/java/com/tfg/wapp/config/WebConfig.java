package com.tfg.wapp.config;

import org.springframework.context.annotation.Configuration;

import com.tfg.wapp.handlers.CustomOAuth2SuccessHandler;
import com.tfg.wapp.services.AppUserDetailsService;
import com.tfg.wapp.services.KeyLoader;

import jakarta.servlet.http.Cookie;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class WebConfig {

	private final AppUserDetailsService userDetailsService;
	private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
	@Autowired
	private KeyLoader keyLoader;
	
	public WebConfig(AppUserDetailsService userDetailsService, CustomOAuth2SuccessHandler customOAuth2SuccessHandler) {
		this.userDetailsService = userDetailsService;
		this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
	}
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
    	CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = 
    			new CustomUsernamePasswordAuthenticationFilter(userDetailsService, passwordEncoder(), keyLoader);
    	
    	customUsernamePasswordAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
    	
    	http.oauth2Login(oauth2 -> oauth2
    			.successHandler(customOAuth2SuccessHandler));
	  
        http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); 
    		
	    http.authorizeHttpRequests(registry -> {
          registry.requestMatchers("/start", "/error", "/home", "/register/**", 
        		  "/css/**", "/js/**", "/webjars/**", "/qr_code", "/verify_otp", "/img/**", "/qr_code_img").permitAll();
          registry.requestMatchers("/premium/**", "/orders/**", "/oauth2works").hasRole("PREMIUM");
          registry.requestMatchers("/user/**", "/design").hasRole("USER");
          registry.anyRequest().authenticated();
	    });
   
	    http.logout(logout -> logout
	            .logoutUrl("/logout") // Default is "/logout"
	            .logoutSuccessUrl("/start") // Redirect to login after logout
	            .addLogoutHandler((request, response, authentication) -> {
	                // Remove the JWT cookie
	                Cookie cookie = new Cookie("JWT", null);
	                cookie.setPath("/");
	                cookie.setMaxAge(0);
	                response.addCookie(cookie);
	            })
	        );
	    
	    http.addFilterAt(customUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	    http.addFilterBefore(new JwtAuthorizationFilter(keyLoader,userDetailsService), customUsernamePasswordAuthenticationFilter.getClass());        
	
	    return http.build();
    	
    }
    
	
    @Bean
    public PasswordEncoder passwordEncoder() throws NoSuchAlgorithmException {
    	SecureRandom s = SecureRandom.getInstanceStrong();
    	return new BCryptPasswordEncoder(4, s);
    }
    
    


}
