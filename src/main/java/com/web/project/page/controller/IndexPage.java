package com.web.project.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/yalolza.gg")
public class IndexPage {

	@GetMapping("")
	public String Index() {
		return "NewFile";
	}

	
}
