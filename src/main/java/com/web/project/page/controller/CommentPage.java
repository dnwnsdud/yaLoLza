package com.web.project.page.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.project.api.controller.CommentService;
import com.web.project.api.controller.CommunityService;
import com.web.project.api.controller.UserService;
import com.web.project.dto.Comment;
import com.web.project.dto.Community;
import com.web.project.dto.SiteUser;
import com.web.project.metrics.Counter;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;

@RequestMapping("/talk.yalolza.gg/comment")
@RequiredArgsConstructor
@Controller
public class CommentPage {
	 private final CommunityService communityService;
	    private final CommentService commentService;
	    private final UserService userService;


	    @PreAuthorize("isAuthenticated()")
	    @PostMapping("/create/{id}")
	    public String createComment(Model model,
	                                @PathVariable("id") Integer id,
	                                @RequestParam(value = "content") String content, Principal principal) {
	        Community community = this.communityService.getCommu(id);
	        SiteUser siteUser = (SiteUser) this.userService.loadUserByUsername(principal.getName());
	        this.commentService.commentcreate(community, content, siteUser);
	        Counter.Increment("commentCount", 1);
	        return String.format("redirect:/talk.yalolza.gg/community/detail/%s", id);
	    }

	    @GetMapping("/delete/{id}")
	    public String deleteComment(@PathVariable("id") Integer id) {
	        Comment comment = this.commentService.getComment(id);
	        this.commentService.delete(comment);
	        Counter.Decrement("commentCount", 1);
	        return String.format("redirect:/talk.yalolza.gg/community/detail/%s", comment.getCommunity().getId());
	    }
	    
}
