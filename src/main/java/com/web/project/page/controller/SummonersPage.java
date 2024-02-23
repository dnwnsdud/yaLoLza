package com.web.project.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/yalolza.gg/summoners")
public class SummonersPage {

	@GetMapping("/{username}")
	public String Summoners(
			@PathVariable("username") String username
			) {
		return "NewFile";
	}

	@GetMapping("/{username}/matches/{puuid}/{gamecreation}")
	public String SummonersDetail(
			@PathVariable("username") String username, 
			@PathVariable("puuid") String puuid,
			@PathVariable("gamecreation") String gamecreation
			) {
		return "NewFile";
	}

}
