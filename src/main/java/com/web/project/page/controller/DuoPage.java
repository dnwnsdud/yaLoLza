package com.web.project.page.controller;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.project.api.controller.Duoservice;
import com.web.project.api.controller.UserService;
import com.web.project.dao.DuoRepository;
import com.web.project.dao.MostChampionsRepository;
import com.web.project.dto.Duo;
import com.web.project.dto.MostChampions;
import com.web.project.dto.SiteUser;
import com.web.project.dto.enumerated.Myposition;
import com.web.project.dto.enumerated.Queuetype;
import com.web.project.dto.enumerated.Yourposition;
import com.web.project.function.Calall;
import com.web.project.metrics.Counter;
import com.web.project.metrics.count.Connect;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/duo.yalolza.gg")
public class DuoPage {

	@Autowired
	private final Duoservice duoService;
	private final UserService userService;

	@Autowired
	Calall cal;
	
	@Autowired
	DuoRepository duoDao;
	@Autowired
	MostChampionsRepository mostChampions;
	
	@Autowired
	PasswordEncoder encoder;


	@Autowired
	public DuoPage(Duoservice duoService, UserService userService) {
		this.duoService = duoService;
        this.userService = userService;
	}

	@GetMapping("/list")
	public String index(Model model) {
		List<Duo> duoList = duoService.getAllDuos();
		model.addAttribute("duoList", duoList);
    	new Connect("total","duo.yalolza.gg", "list");
		return "duoList";
	}

	@GetMapping("/save")
	public String saveForm() {
    	new Connect("total","duo.yalolza.gg", "save");
		return "duoSave";
	}
	
//	@GetMapping("/duo/{name}")  
//	public String getDuoInfo(@PathVariable String  name, Model model) {
//	    try {
//	        // 실시간 정보를 가져옴
//	        List<Object[]> most = cal.calDuoMost(name);
//	        
//	        // MostChampions 엔티티에 데이터 매핑하여 저장
//	        for (Object[] data : most) {
//	            MostChampions mostChampion = new MostChampions();
//	            mostChampion.setMostChampion(data[0].toString());
//	            mostChampion.setRound(Long.parseLong(data[1].toString()));
//	            mostChampion.setWins(Long.parseLong(data[2].toString()));
//	            mostChampion.setKills(Double.parseDouble(data[3].toString()));
//	            mostChampion.setDeaths(Double.parseDouble(data[4].toString()));
//	            mostChampion.setAssists(Double.parseDouble(data[5].toString()));
//	            // 필요한 정보에 대해 추가 설정
//	            
//	            // MostChampions 엔티티를 디비에 저장
//	            mostChampions.save(mostChampion);
//	        }
//
//	        // 모델에 실시간 정보를 추가하여 뷰로 전달
//	        model.addAttribute("most", most);
//	        
//	        return "testduopage"; // 실시간 정보를 표시할 페이지로 이동
//	    } catch (Exception e) {
//	        return "message"; // 예외 처리 페이지로 이동
//	    }
//	}
		
	@PostMapping("/create")
	public String saveDuo(@ModelAttribute Duo duoDto, Model model, RedirectAttributes redirectAttributes, Principal principal) {
	    // 폼에서 받은 Duo 객체를 받음

	    if (!duoDto.getDuopassword1().equals(duoDto.getDuopassword2())) {
	        model.addAttribute("message", "듀오 등록 실패: 2개의 패스워드가 일치하지 않습니다.");
	        model.addAttribute("searchUrl", "/duo.yalolza.gg/save");
	        return "message"; // 패스워드가 일치하지 않을 경우 메시지 페이지 반환
	    }
	    
	    try {
	        List<Object[]> most = cal.calDuoMost(duoDto.getSummonerName());
	   
	        if (most.isEmpty()) {
	            model.addAttribute("message", "듀오 등록 실패: MostChampions 정보가 없습니다.");
	            model.addAttribute("searchUrl", "/duo.yalolza.gg/list");
	            return "message"; 
	        }

	        duoDao.save(duoDto);

	        for (Object[] data : most) {
	            MostChampions mostChampion = new MostChampions();
	            mostChampion.setMostChampion(data[0].toString());
	            mostChampion.setRound(Long.parseLong(data[1].toString()));
	            mostChampion.setWins(Long.parseLong(data[2].toString()));
	            mostChampion.setKills(Double.parseDouble(data[3].toString()));
	            mostChampion.setDeaths(Double.parseDouble(data[4].toString()));
	            mostChampion.setAssists(Double.parseDouble(data[5].toString()));
	            mostChampion.setDuo(duoDto); // 해당 MostChampion이 어떤 Duo에 속하는지 설정

	            // MostChampions 엔티티를 디비에 저장
	            mostChampions.save(mostChampion);
	        }

	        // 리다이렉트
	        return "redirect:/duo.yalolza.gg/list"; 
	    } catch (Exception e) {
	        model.addAttribute("message", "소환사 검색 결과가 없습니다. 리스트로 돌아갑니다.: " + e.getMessage());
	        model.addAttribute("searchUrl", "/duo.yalolza.gg/list");
	        return "message"; // 저장 중 문제 발생 시 메시지 페이지 반환
	    }
	}

	@GetMapping("/searchByPosition")
	@ResponseBody
	public List<Duo> searchDuoByPosition(@RequestParam Myposition myposition) {
		if (myposition == Myposition.ALL) {
			return duoService.getAllDuos();
		} else {
			return duoService.getDuoByPosition(myposition);
		}
	}

	@GetMapping("/searchByQueuetype")
	@ResponseBody
	public List<Duo> searchDuoByQueuetype(@RequestParam Queuetype queuetype) {
		return duoService.getDuoByQueuetype(queuetype);
	}
	
	@GetMapping("/searchByYourposition")
	@ResponseBody
	public List<Duo> searchDuoByYourposition(@RequestParam Yourposition  yourposition) {
		return duoService.getDuoByYourposition(yourposition);
	}

//	@GetMapping("/view/{id}")
//	public String view(@PathVariable Long id, Model model) {
//		
//		  Duo duoview = duoService.duoview(id);
//		  Optional<MostChampions> mostChampions = duoService.findMostChampionsByDuoId(id);
//		
//		  model.addAttribute("duoview", duoview);
//		  model.addAttribute("mostChampions", mostChampions);
//    	new Connect("total","duo.yalolza.gg", "view");
//		return "duoView";
//	}
	@GetMapping("/view/{id}")
	public String view(@PathVariable Long id, Model model) {
	    Duo duoview = duoService.duoview(id);
	    List<MostChampions> mostChampionsList = duoService.findMostChampionsByDuoId(id);
	    
	    model.addAttribute("duoview", duoview);
	    model.addAttribute("mostChampions", mostChampionsList);
	    new Connect("total","duo.yalolza.gg", "view");
	    return "duoView";
	}
 
	@GetMapping("/edit/{id}")
	public String editDuo(@PathVariable Long id, Model model) {
		Duo duoDto = duoDao.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 Article이 없습니다."));
		model.addAttribute("duoDto", duoDto);
    	new Connect("total","duo.yalolza.gg");
		return "duoEdit";
	}
 
	@PostMapping("/update/{id}")
	public String duoUpdate(@PathVariable("id") Long id, @Valid Duo duoDto,
	        @RequestParam(value = "duopassword2", required = false) Long duopassword2, Model model) {
	    log.info("form: " + duoDto.toString());

	    Duo duotemp = duoService.duoview(id);

	    if (!duoDto.getDuopassword1().equals(duoDto.getDuopassword2())) {
		    
		        model.addAttribute("message","듀오 수정 실패 패스워드가 일치하지 않습니다.");
		        model.addAttribute("searchUrl","/duo.yalolza.gg/edit/" + id);
		        return "message"; // 리다이렉트 대신 페이지에 그대로 남도록 수정
		}else {

//			model.addAttribute("message","듀오 수정 성공"); 
//	    	model.addAttribute("searchUrl","/duo.yalolza.gg/duo/list"); 
	    	
	    	duotemp.setMyposition(duoDto.getMyposition());
	    	duotemp.setYourposition(duoDto.getYourposition());
	    	duotemp.setQueuetype(duoDto.getQueuetype());
	    	duotemp.setDuopassword2(duoDto.getDuopassword2());
	    	duotemp.setMemo(duoDto.getMemo());
	    	duotemp.setIsmike(duoDto.getIsmike());
			duotemp.setLastModifiedDate(duoDto.getLastModifiedDate());
			
			duoDao.save(duotemp);
		    
//		return "message"; 
			return "redirect:/duo.yalolza.gg/list"; 
		    }
	}

	@PostMapping("/delete/{id}")
	public String duoDelete(@PathVariable("id") Long id, @Valid Duo duoDto,
	        @RequestParam(value = "duopassword2", required = false) Long duopassword2, Model model) {
	    log.info("form: " + duoDto.toString());

	    Duo duotemp = duoService.duoview(id);

	    if (!duoDto.getDuopassword1().equals(duoDto.getDuopassword2())) {
		   
		        model.addAttribute("message","듀오 삭제 실패 패스워드가 일치하지 않습니다.");
		        model.addAttribute("searchUrl","/duo.yalolza.gg/view/" + id);
		        
		        return "message";
		    }else { 
//		    	model.addAttribute("message","듀오 삭제 성공"); 
//		    	model.addAttribute("searchUrl","/duo.yalolza.gg/duo/list"); 
		    	duotemp.setDuopassword2(duoDto.getDuopassword2());
					duoService.duoDelete(id);
		    
//				return "message"; 
			        Counter.Decrement("duoCount", 1);
					return "redirect:/duo.yalolza.gg/list"; 
		    }

	}
}