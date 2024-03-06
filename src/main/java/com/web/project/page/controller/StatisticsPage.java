package com.web.project.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.project.metrics.count.Connect;

@Controller
@RequestMapping("/yalolza.gg/statistics")
public class StatisticsPage {

	@GetMapping("")
	public String root() {
		return "redirect:/yalolza.gg/statistics/champions";
	}
	
	
	@GetMapping("/champions")
	public String StatisticsChampions() {
    	new Connect("total","yalolza.gg", "statistics", "champions");
		return "NewFile";
	}


	@GetMapping("/tiers")
	public String StatisticsTiers() {
    	new Connect("total","yalolza.gg", "statistics", "tiers");
		return "NewFile";
	}

}
