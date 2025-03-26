package com.tfg.wapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tfg.wapp.entities.AppUser;
import com.tfg.wapp.repositories.AppUserRepository;
import com.tfg.wapp.services.TwoFactorAuthService;

@Controller
@RequestMapping("/register")
public class RegistrationUserController {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TwoFactorAuthService tAuthService;
    
    @ModelAttribute(name = "appUser")
    public AppUser appUser() {
    	return new AppUser();
    }

    @GetMapping("/user")
    public String register() {
    	return "register";
    }
    
    @PostMapping
    public String createUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRole().equals("PREMIUM"))
        	user.setRole(user.getRole() + ",USER");
        
        user.setSecret(tAuthService.generateSecretKey());
        
        appUserRepository.save(user);
        
        return "redirect:/home";
    }
}
