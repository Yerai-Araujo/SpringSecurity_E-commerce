package com.tfg.wapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {

	  @GetMapping("/home")
	  public String home() {
	    return "home";
	  }
	
	  @GetMapping("/premium/home")
	  public String handlePremiumHome() {
	    return "premium_home";
	  }

	  @GetMapping("/user/home")
	  public String handleUserHome() {
	    return "user_home";
	  }

	  @GetMapping("/oauth2_Works")
	  public String handleoauth2() {
	    return "oauth2_Works";
	  }
	  
	  @GetMapping("/start")
	  public String handleStart() {
		  return "start";
	  }
	  
}
