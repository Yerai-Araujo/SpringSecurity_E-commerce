package com.tfg.wapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.wapp.entities.AppUser;
import com.tfg.wapp.services.AppUserDetailsService;
import com.tfg.wapp.services.TwoFactorAuthService;

@Controller
public class TwoFactorAuthController {

    private final TwoFactorAuthService twoFactorAuthService;
    private final AppUserDetailsService appUserDetailsService;
    private String secretKey;
    private String username;

    public TwoFactorAuthController(TwoFactorAuthService twoFactorAuthService, AppUserDetailsService appUserDetailsService, QRCodeController qrCodeController) {
        this.twoFactorAuthService = twoFactorAuthService;
        this.appUserDetailsService = appUserDetailsService;
    }

    @PostMapping("/verify_otp")
    public String verifyOtp(@RequestParam("otp") int otp, Model model) {

        if (twoFactorAuthService.isOtpValid(secretKey, otp)) {
        	return "redirect:/home"; // Redirect to a secured page
        } else {
            return "redirect:/qr_code?username=" + username + "&error=Invalid OTP";
        }
    }
    
    @GetMapping("/qr_code")
    public String getQrCodePage(@RequestParam("username") String username, Model model) {
    	
    	// Fetch secret key from DB
    	AppUser appUser = appUserDetailsService.getUserByUsername(username);
    	username = appUser.getUsername();
        secretKey = appUser.getSecret();
        
        model.addAttribute("username", username);
        model.addAttribute("secretKey", secretKey);
    	
        return "qr_code"; // This loads qr-code.html
    }
    
    
}
