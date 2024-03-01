package com.web.project.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.project.metrics.count.Connect;

@Controller
@RequestMapping("/yalolza.gg/ranking")
public class RankingPage {

	@GetMapping("")
	public String root() {
		return "redirect:/tier";
	}

	@GetMapping("/tier")
	public String rankingTier() {
    	new Connect("total","yalolza.gg", "ranking", "tier");
		return "NewFile";
	}
	@GetMapping("/champions")
	public String rankingChampions() {
    	new Connect("total","yalolza.gg", "ranking", "champions");

		return "NewFile";
	}
	@GetMapping("/level")
	public String rankinglevel() {
    	new Connect("total","yalolza.gg", "ranking", "level");
		return "NewFile";
	}
	


}
