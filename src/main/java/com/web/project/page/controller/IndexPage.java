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
import com.web.project.metrics.count.Connect;

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
    	new Connect("total","duo.yalolza.gg", "index");
        return "index";
    }

	@GetMapping("/useterms")
	public String useterms() {
    	new Connect("total","duo.yalolza.gg", "useterms");
		return "useterms";
	}
	
	@GetMapping("/privacy")
	public String privacy() {
    	new Connect("total","duo.yalolza.gg", "privacy");
		return "privacy";
	}
	
}
