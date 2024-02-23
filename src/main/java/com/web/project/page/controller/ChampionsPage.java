package com.web.project.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/yalolza.gg/champions")
public class ChampionsPage {


	@GetMapping("/")
	public String Champions(
			@RequestParam(required = false) String position
			) {
		return "NewFile";
	}
	
	@GetMapping("/{champion}/build/{position}")
	public String ChampionsDetail(
			@PathVariable("champion") String champion, 
			@PathVariable("position") String position
			) {
		return "";
	}

	



}
