package com.web.project.page.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.project.api.controller.CommunityService;
import com.web.project.api.controller.UserService;
import com.web.project.dto.Community;
import com.web.project.dto.PasswordForm;
import com.web.project.dto.SiteUser;
import com.web.project.dto.UserCreateForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminPage {

	@Autowired
	private final UserService userService;
	
    @Autowired
    private CommunityService communityService;
	
	@GetMapping("/dashboard")
   	public String adminpage(@AuthenticationPrincipal SiteUser user, Model model) {
		System.out.println("관리자 페이지 들어간다");
       	model.addAttribute("user", user);
       	return "/admin/dashboard";
   	}
	
	 @GetMapping("/userlist")
	 public String getUserList(Model model) {
		 List<SiteUser> userList = userService.getAllUsers();
		 model.addAttribute("users", userList);
	 	return "/admin/userlist"; // 유저 리스트를 표시하는 페이지로 이동
	 }
	
	 @PostMapping("/deleteuser")
	 public String deleteUser(@RequestParam Long id) {
		 userService.deleteUser(id);
		 return "redirect:/admin/userlist"; // 삭제 후 유저 리스트 페이지로 리다이렉트
	    }
	
	 @GetMapping("/communitylist")
	 public String getCommunityList(Model model) {
		 List<Community> communityList = communityService.getAllCommunities();
		 	model.addAttribute("communities", communityList);
		 	return "admin/communitylist";
	    }

	 @PostMapping("/deletecommunity")
	 public String deleteCommunity(@RequestParam Integer id) {
		 communityService.deleteCommunity(id);
		 return "redirect:/admin/communitylist";
	    }
	 
	 @GetMapping("/viewcommunity")
 	 public String viewCommunity(@RequestParam Integer id, Model model) {
		 Community community = communityService.getCommunity(id);
		 	model.addAttribute("community", community);
		 	return "commu_detail";
	    }

}
