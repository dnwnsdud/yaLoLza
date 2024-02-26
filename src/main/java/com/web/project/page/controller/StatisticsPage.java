package com.web.project.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/yalolza.gg/statistics")
public class StatisticsPage {

	@GetMapping("")
	public String root() {
		return "redirect:/yalolza.gg/statistics/champions";
	}
	
	
	@GetMapping("/champions")
	public String StatisticsChampions() {
		return "NewFile";
	}


	@GetMapping("/tiers")
	public String StatisticsTiers() {
		return "NewFile";
	}

}
