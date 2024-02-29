package com.web.project.page.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.project.api.controller.CommunityService;
import com.web.project.api.controller.UserService;
import com.web.project.dao.CommunityRepository;
import com.web.project.dto.Community;
import com.web.project.metrics.Counter;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/yalolza.gg")
public class IndexPage {

	 private final CommunityRepository communityRepository;
	 
	 
//	@GetMapping("/")
//	public String index(Model model){
//		return "index";
//    }
	
	@GetMapping("/")
    public String index(Model model){
        List<Community> communityList = this.communityRepository.findTop10ByOrderByCreateDateDesc();
        model.addAttribute("communityList", communityList);
        return "index";
    }

	@GetMapping("/useterms")
	public String useterms() {
		return "useterms";
	}
	
	@GetMapping("/privacy")
	public String privacy() {
		return "privacy";
	}
	
}
