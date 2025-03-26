package com.tfg.wapp.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tfg.wapp.entities.AppUser;
import com.tfg.wapp.repositories.AppUserRepository;

import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository uRepository;
    
    
    public AppUserDetailsService(AppUserRepository uRepository) {
    	this.uRepository = uRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> user = uRepository.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(getRoles(userObj))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
    
    public AppUser getUserByUsername(String username) {
        return uRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private String[] getRoles(AppUser user) {
        if (user.getRole() == null) {
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
    }
    
    
    
}
