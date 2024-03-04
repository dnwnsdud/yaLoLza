package com.web.project.page.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.project.function.Calall;


 
@Controller
@RequestMapping("/newlolkia22") 
public class DuoTestController {
	@Autowired
	Calall cal;

    @GetMapping("/DDUUOO/{name}")  // 원하는 URL 매핑을 설정합니다.
    public String yourMethod(@PathVariable String  name, Model model) {
     System.out.println("왔으");

     List<Object[]> most = null;
	try {
		most = cal.calDuoMost(name);
	} catch (Exception e) {
		return "xpage";
	}
    	
    	
        model.addAttribute("most", most);

        return "testduopage";
    }
}