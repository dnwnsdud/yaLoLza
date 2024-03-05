package com.web.project.page.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import com.web.project.dto.championstats.AramChampionDTO;
import com.web.project.system.AramDataService;


@Controller
public class AramChampionController {

    @Autowired
    private AramDataService aramDataService;

    @GetMapping("/champions")
    public String showChampions(Model model) {
        List<AramChampionDTO> aramChampions = aramDataService.getAramChampionData();
        model.addAttribute("aramChampions", aramChampions);
        return "aram"; 
    }
}