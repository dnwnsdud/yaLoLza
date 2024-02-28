package com.web.project.page.controller;


import java.security.Principal;
import java.util.List;

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

import com.web.project.api.controller.Duoservice;
import com.web.project.api.controller.UserService;
import com.web.project.dao.DuoRepository;
import com.web.project.dto.Duo;
import com.web.project.dto.SiteUser;
import com.web.project.dto.enumerated.Myposition;
import com.web.project.dto.enumerated.Queuetype;
import com.web.project.dto.enumerated.Yourposition;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/duo.yalolza.gg/duo")
public class DuoPage {

	@Autowired
	private final Duoservice duoService;
	private final UserService userService;

	@Autowired
	DuoRepository duoDao;
	
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
		return "duoList";
	}

	@GetMapping("/save")
	public String saveForm() {
		return "duoSave";
	}
	
	 @PostMapping("/create")
	 public String saveDuo(@ModelAttribute Duo duoDto, Model model, Principal principal) {
	        // 폼에서 받은 Duo 객체를 받음

		 if (!duoDto.getDuopassword1().equals(duoDto.getDuopassword2())) {
	            model.addAttribute("message", "듀오 등록 실패: 2개의 패스워드가 일치하지 않습니다.");
	            model.addAttribute("searchUrl","/duo.yalolza.gg/duo/save");
	            return "message"; // 패스워드가 일치하지 않을 경우 메시지 페이지 반환
	        }

		 if (principal != null) { // 로그인한 사용자인 경우
	            String username = principal.getName(); // 현재 사용자의 아이디 가져오기
	            SiteUser user = (SiteUser) userService.loadUserByUsername(username); // 사용자의 아이디로 사용자 엔티티 조회

	            if (user != null) {
	                duoDto.setSiteUser(user); // 조회한 사용자 엔티티를 Duo 엔티티에 설정
	            }
	        }

	        try {
	            duoDao.save(duoDto); // Duo 엔티티 저장
	            return "redirect:/duo.yalolza.gg/duo/list"; // 리스트 페이지로 리다이렉트
	        } catch (Exception e) {
	            model.addAttribute("message", "듀오 등록 실패: " + e.getMessage());
	            model.addAttribute("searchUrl","/duo.yalolza.gg/duo/save");
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

	@GetMapping("/view/{id}")
	public String view(@PathVariable Long id, Model model) {
		model.addAttribute("duoview", duoService.duoview(id));
		return "duoView";
	}

	@GetMapping("/edit/{id}")
	public String editDuo(@PathVariable Long id, Model model) {
		Duo duoDto = duoDao.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 Article이 없습니다."));
		model.addAttribute("duoDto", duoDto);
		return "duoEdit";
	}

	@PostMapping("/update/{id}")
	public String duoUpdate(@PathVariable("id") Long id, @Valid Duo duoDto,
	        @RequestParam(value = "duopassword2", required = false) Long duopassword2, Model model) {
	    log.info("form: " + duoDto.toString());

	    Duo duotemp = duoService.duoview(id);

	    if (!duoDto.getDuopassword1().equals(duoDto.getDuopassword2())) {
		    
		        model.addAttribute("message","듀오 수정 실패 패스워드가 일치하지 않습니다.");
		        model.addAttribute("searchUrl","/duo.yalolza.gg/duo/edit/" + id);
		        return "message"; // 리다이렉트 대신 페이지에 그대로 남도록 수정
		}else {

//			model.addAttribute("message","듀오 수정 성공"); 
//	    	model.addAttribute("searchUrl","/yalolza.gg/duo/list"); 
	    	
	    	duotemp.setMyposition(duoDto.getMyposition());
	    	duotemp.setYourposition(duoDto.getYourposition());
	    	duotemp.setQueuetype(duoDto.getQueuetype());
	    	duotemp.setDuopassword2(duoDto.getDuopassword2());
	    	duotemp.setMemo(duoDto.getMemo());
	    	duotemp.setIsmike(duoDto.getIsmike());
		    duotemp.setTier(duoDto.getTier());
			duotemp.setLastModifiedDate(duoDto.getLastModifiedDate());
			
			duoDao.save(duotemp);
		    
//		return "message"; 
			return "redirect:/duo.yalolza.gg/duo/list"; 
		    }
	}

	@PostMapping("/delete/{id}")
	public String duoDelete(@PathVariable("id") Long id, @Valid Duo duoDto,
	        @RequestParam(value = "duopassword2", required = false) Long duopassword2, Model model) {
	    log.info("form: " + duoDto.toString());

	    Duo duotemp = duoService.duoview(id);

	    if (!duoDto.getDuopassword1().equals(duoDto.getDuopassword2())) {
		   
		        model.addAttribute("message","듀오 삭제 실패 패스워드가 일치하지 않습니다.");
		        model.addAttribute("searchUrl","/duo.yalolza.gg/duo/view/" + id);
		        
		        return "message";
		    }else { 
//		    	model.addAttribute("message","듀오 삭제 성공"); 
//		    	model.addAttribute("searchUrl","/yalolza.gg/duo/list"); 
		    	duotemp.setDuopassword2(duoDto.getDuopassword2());
					duoService.duoDelete(id);
		    
//				return "message"; 
					return "redirect:/duo.yalolza.gg/duo/list"; 
		    }

	}
}