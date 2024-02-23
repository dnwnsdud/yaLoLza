package com.web.project.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/yalolza.gg/ranking")
public class RankingPage {

	@GetMapping("/")
	public String root() {
		return "redirect:/tier";
	}

	@GetMapping("/tier")
	public String rankingTier() {
		return "NewFile";
	}
	@GetMapping("/champions")
	public String rankingChampions() {
		return "NewFile";
	}
	@GetMapping("/level")
	public String rankinglevel() {
		return "NewFile";
	}
	


}
