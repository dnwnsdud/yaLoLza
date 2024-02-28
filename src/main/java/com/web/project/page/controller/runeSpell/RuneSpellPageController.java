package com.web.project.page.controller.runeSpell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/statistics")
public class RuneSpellPageController {

    @GetMapping("/rune")
    public String rune() {
        return "rune"; 
    }
    

    @GetMapping("/howlingabyss")
    public String myPage() {
        return "howlingabyss"; 
    }
}
