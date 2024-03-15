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

import com.web.project.api.controller.CommentService;
import com.web.project.api.controller.CommunityService;
import com.web.project.api.controller.UserService;
import com.web.project.dto.Comment;
import com.web.project.dto.Community;
import com.web.project.dto.PasswordForm;
import com.web.project.dto.SiteUser;
import com.web.project.dto.UserCreateForm;
import com.web.project.metrics.Counter;

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

	@Autowired
	private CommentService commentService;

	@GetMapping("/dashboard")
	public String adminpage(@AuthenticationPrincipal SiteUser user, Model model) {

		model.addAttribute("user", user);

		List<SiteUser> users = userService.getAllUsers();
		List<Community> communities = communityService.getAllCommunities();
		List<Comment> comments = commentService.getAllComments();

		model.addAttribute("users", users);
		model.addAttribute("communities", communities);
		model.addAttribute("comments", comments);

		return "/admin/dashboard";
	}

	@PostMapping("/delete_user")
	public String deleteUser(@RequestParam Long id) {
		userService.deleteUser(id);
		Counter.Decrement("userCount", 1);
		return "redirect:/admin/dashboard";
	}

	@PostMapping("/delete_community")
	public String deleteCommunity(@RequestParam Integer id) {
		communityService.deleteCommunity(id);
		Counter.Decrement("commuCount",1);
		return "redirect:/admin/dashboard";
	}

	@PostMapping("/delete_comment")
	public String deleteComment(@RequestParam Integer id) {
		commentService.deleteComment(id);
		Counter.Decrement("commentCount", 1);
		return "redirect:/admin/dashboard";
	}

	@GetMapping("/view_community")
	public String viewCommunity(@RequestParam Integer id, Model model) {
		Community community = communityService.getCommunity(id);
		model.addAttribute("community", community);
		return "commu_detail";
	}

}
