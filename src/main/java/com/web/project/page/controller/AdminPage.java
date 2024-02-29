package com.web.project.page.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.project.api.controller.UserService;
import com.web.project.dto.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminPage {

	@Autowired
	PasswordEncoder encoder;
	private final UserService userService;
	
	@GetMapping("/dashboard")
   	public String adminpage(@AuthenticationPrincipal SiteUser user, Model model) {
		System.out.println("관리자 들어간다");
       	model.addAttribute("user", user); 
       	return "/admin/dashboard";
   	}
}
