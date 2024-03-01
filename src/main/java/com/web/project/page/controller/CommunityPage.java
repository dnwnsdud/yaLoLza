package com.web.project.page.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.web.project.api.controller.CommunityService;
import com.web.project.api.controller.UserService;
import com.web.project.dao.CommunityRepository;
import com.web.project.dto.Community;
import com.web.project.dto.CommunityForm;
import com.web.project.dto.SiteUser;
import com.web.project.dto.enumerated.CommunityEnum;
import com.web.project.metrics.Counter;
import com.web.project.metrics.count.Connect;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/talk.yalolza.gg/community")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CommunityPage {
	 private final CommunityRepository communityRepository;
	    private final CommunityService communityService;
	    private final UserService userService;

	    @GetMapping("/")
	    public String first() {
	        return "redirect:/talk.yalolza.gg/community/list/all";
	    }
	    //    @GetMapping("/list")
//	    public String list(Model model){
//	        List<Community> clist = this.communityService.getList();
//	        model.addAttribute("community", clist);
//	        return "community";
//	    }

	    @GetMapping("/index")
	    public String index(Model model){
	        List<Community> communityList = this.communityRepository.findTop10ByOrderByCreateDateDesc();
	        model.addAttribute("communityList", communityList);
	    	new Connect("total","talk.yalolza.gg", "community", "index");
	        return "index";
	    }

	    @GetMapping("/list/qna")
	    public String qnalist(Model model,
	                       @RequestParam(value = "page", defaultValue = "0") int page,
	                       @RequestParam(value="keyword", defaultValue = "") String keyword){
	         String category = "QnA";

	        Page<Community> paging = communityService.getList(page, category, keyword);

	        model.addAttribute("paging", paging);
	        model.addAttribute("category", category);
	    	new Connect("total","yalolza.gg", "community", "qna");
	        return "community";
	    }

	    @GetMapping("/list/{type}")
	    public String list(Model model,
	                       @RequestParam(value="page", defaultValue="0") int page,
	                       @PathVariable String type,
	                       @RequestParam(value="keyword", defaultValue = "") String keyword) {
	        String category = switch (type) {
	            case "all" -> CommunityEnum.All.getGroup();
	            case "free" -> CommunityEnum.FREE.getGroup();
	            case "humer" -> CommunityEnum.HUMER.getGroup();
	            case "video" -> CommunityEnum.VIDEO.getGroup();
	            case "best" -> CommunityEnum.BEST.getGroup();
	            default -> throw new RuntimeException("올바르지 않습니다.");
	        };
	        model.addAttribute("boardName", category);
	        Page<Community> paging = communityService.getList(page, category, keyword);
	        model.addAttribute("paging", paging);
	    	new Connect("total","talk.yalolza.gg", "community", "list", type);
	        return "community";
	    }

	    @GetMapping(value="/detail/{id}")
	    public String detail(Model model, @PathVariable("id") Integer id) {

	        Community community = this.communityService.getCommu(id);
	        model.addAttribute("community", community);
	    	new Connect("total","talk.yalolza.gg", "community", "detail");
	        return "commu_detail";
	    }

	    @PreAuthorize("isAuthenticated()")
	    @GetMapping("/create/{type}")
	    public String CommuCreate(CommunityForm communityForm,
	                              @PathVariable String type,
	                              Model model){
	        switch (type) {
	            case "free" -> model.addAttribute("boardName", "자유게시판");
	            case "humer" -> model.addAttribute("boardName", "유머게시판");
	            case "video" -> model.addAttribute("boardName", "영상게시판");
	            case "qna" -> model.addAttribute("boardName", "QnA");
	            default -> throw new RuntimeException("올바르지 않습니다.");
	        }
	    	new Connect("total","talk.yalolza.gg", "community", "create", type);

	        return "commu_form";
	    }
	//@ModelAttribute("communityEnums")
	//public CommunityEnum[] communityEnums(){
//	        return CommunityEnum.values();
	//}


//	    @PostMapping("/create")
//	    public String CommuCreate(@RequestParam(value="title") String title, @RequestParam(value="content") String content) {
//	        return "redirect:/community/list";
//	    }

//	    @PostMapping("/create/{type}")
//	    public String CommuCreate(@Valid CommunityForm communityForm,
//	                              @PathVariable String type,
//	                              BindingResult bindingResult) throws IOException {
//	        if(bindingResult.hasErrors()){
//	            return "commu_form";
//	        }
//	        String category = switch (type) {
//	            case "free" -> CommunityEnum.FREE.getGroup();
//	            case "humer" -> CommunityEnum.HUMER.getGroup();
//	            case "video" -> CommunityEnum.VIDEO.getGroup();
//	            default -> throw new RuntimeException("올바르지 않습니다.");
//	        };
	//
//	        communityService.create(communityForm.getTitle(),
//	                communityForm.getContent(), category);
//	        return "redirect:/community/list/%s".formatted(type);
//	    }

	    @PreAuthorize("isAuthenticated()")
        @PostMapping("/create/qna")
	    public String CommuCreate(@RequestParam(value="title") String title,
	    		@RequestParam(value="content") String content,
									        @Valid CommunityForm communityForm,
								            BindingResult bindingResult,
									        String category, List<MultipartFile> file,
									        Principal principal) throws IOException {
	    	if(bindingResult.hasErrors()) {
	    		return "commu_form";
	    	}
	       
	    	category = "QnA";
	            SiteUser siteUser = (SiteUser) this.userService.loadUserByUsername(principal.getName());
	            communityService.create(communityForm.getTitle(),
	                    communityForm.getContent(), category, file, siteUser);
		        Counter.Increment("qnaCount",1);
	        return "redirect:/talk.yalolza.gg/community/list/qna";
	    }
 
	    @PreAuthorize("isAuthenticated()")
	    @PostMapping("/create/{type}")
	    public String CommuCreate(@Valid CommunityForm communityForm,
                					BindingResult bindingResult,
	                              @PathVariable String type,
	                              List<MultipartFile> file,
	                              Principal principal) throws IOException {
	        SiteUser siteUser = (SiteUser) this.userService.loadUserByUsername(principal.getName());
	        if(bindingResult.hasErrors()){
	            return "commu_form";
	        }
	     
	        String category = switch (type) {
	            case "all" -> CommunityEnum.All.getGroup();
	            case "free" -> CommunityEnum.FREE.getGroup();
	            case "humer" -> CommunityEnum.HUMER.getGroup();
	            case "video" -> CommunityEnum.VIDEO.getGroup();
	            default -> throw new RuntimeException("올바르지 않습니다.");
	        };

	        communityService.create(communityForm.getTitle(),
	                communityForm.getContent(), category, file, siteUser);
	        Counter.Increment("commuCount",1);
	        return "redirect:/talk.yalolza.gg/community/list/%s".formatted(type);
	    }

	    @GetMapping("/delete/{id}")
	    public String commuDelete(
	            @PathVariable("id") Integer id){
	        Community community= this.communityService.getCommu(id);
//	    if(!community.getAuthor().getU) 회원 넘어오면 해야돼
	        this.communityService.delete(community);
	        Counter.Decrement("commuCount",1);
	        return "redirect:/talk.yalolza.gg/community/";
	    }


	    @PreAuthorize("isAuthenticated()")
	    @GetMapping("/modify/{id}")
	    public String modifywrite(CommunityForm communityForm,
	                              @PathVariable("id") Integer id, Principal principal) {
	        Community community = this.communityService.getCommu(id);
	        if(!community.getSiteUser().getUsername().equals(principal.getName())){
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
	        }
	        communityForm.setTitle(community.getTitle());
	        communityForm.setContent(community.getContent());
	    	new Connect("total","talk.yalolza.gg", "community");
	        return "commu_form";
	    }

	    @PreAuthorize("isAuthenticated()")
	    @PostMapping("/modify/{id}")
	    public String modifywrite(@Valid CommunityForm communityForm,
	                              BindingResult bindingResult,
	                              @PathVariable("id") Integer id,
	                              List<MultipartFile> file,
	                              Principal principal) throws IOException {
	        Community community = this.communityService.getCommu(id);
	        if(!community.getSiteUser().getUsername().equals(principal.getName())){
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
	        }
	        this.communityService.modify(community, communityForm.getTitle(), communityForm.getContent(), file);
	        return String.format("redirect:/talk.yalolza.gg/community/detail/%s", id);
	    }

	    
}
