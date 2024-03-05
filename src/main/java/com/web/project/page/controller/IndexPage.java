package com.web.project.page.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.project.api.controller.CommunityService;
import com.web.project.api.controller.Duoservice;
import com.web.project.api.controller.UserService;
import com.web.project.api.controller.YoutubeMainService;
import com.web.project.dao.CommunityRepository;
import com.web.project.dao.DuoRepository;
import com.web.project.dto.Community;
import com.web.project.dto.Duo;
import com.web.project.metrics.Counter;
import com.web.project.metrics.Timer;
import com.web.project.metrics.count.Connect;

import io.micrometer.core.instrument.Metrics;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/yalolza.gg")
public class IndexPage {

	 private final CommunityRepository communityRepository;
	
	 @Autowired
	 private final Duoservice duoService;
		
	 @Autowired
	    private YoutubeMainService youtubeService;
	 
	 
	 
	@GetMapping("")
	public String index1(Model model){
		return "index";
    }
	
	@GetMapping("/")
    public String index(Model model){
		
			List<Community> communityList = this.communityRepository.findTop10ByOrderByCreateDateDesc();
			List<Duo> duoList = duoService.getAllDuos();
	        model.addAttribute("communityList", communityList);
			model.addAttribute("duoList", duoList);
	        model.addAttribute("youtubeVideos", youtubeService.youtubeGenerator());
	    	new Connect("total","duo.yalolza.gg", "index");
        return "index";
    }

	@GetMapping("/useterms")
	public String useterms() {
    	new Connect("total","yalolza.gg", "useterms");
		return "useterms";
	}
	
	@GetMapping("/privacy")
	public String privacy() {
    	new Connect("total","yalolza.gg", "privacy");
		return "privacy";
	}
	
}
