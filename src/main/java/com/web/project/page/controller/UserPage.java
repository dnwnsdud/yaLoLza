package com.web.project.page.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.project.api.controller.UserService;
import com.web.project.dto.PasswordForm;
import com.web.project.dto.SiteUser;
import com.web.project.dto.UserCreateForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/yalolza.gg/user")
public class UserPage {

    @Autowired
    PasswordEncoder encoder;
    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getNickname(), userCreateForm.getEmail(), encoder.encode(userCreateForm.getPassword1()));
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "login_form";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @GetMapping("/unregist")
    public String userDelete(@AuthenticationPrincipal SiteUser user, Model model, @Valid PasswordForm passwordForm) {
        model.addAttribute("user", user);
        return "/mypage_form";
    }

    @PostMapping("/unregist")
    public String userDelete(@Valid PasswordForm passwordForm, BindingResult bindingResult, SiteUser user, Long id) {
        if (bindingResult.hasErrors()) {
            return "/mypage_form";
        }
        userService.deleteUser(id);
        return "redirect:/yalolza.gg/user/logout";

    }
    
    @GetMapping("/mypage")
    public String userMypage(@AuthenticationPrincipal SiteUser user, Model model) {
        model.addAttribute("user", user);
        return "mypage_form";
    }

    @PostMapping("/mypage/change-username")
    public String changeUsername (@RequestParam("newNickname")  String newNickname, Model model) {
    	boolean result = userService.changeNickname(newNickname);
    	if(!result) {
    		model.addAttribute("usernameError", "이미 존재하는 닉네임 입니다.");
    		return "mypage_form";
    	}
    	return "mypage_form";
    }

    @PostMapping("/mypage/change-pass")
	public String changeUserPass (@RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass, @RequestParam("newPassConfirm") String newPassConfirm, @AuthenticationPrincipal SiteUser currentUser, RedirectAttributes redirectAttributes, Model model) {
		Long userId = currentUser.getId();

		boolean result = userService.changePass(userId, oldPass, newPass, newPassConfirm);
		
		if (result) {
			redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
	        return "mypage_form";
	    } else {
	        model.addAttribute("errorMessage", "비밀번호 변경에 실패했습니다. 입력 정보를 확인해 주세요.");
	        return "mypage_form";
	    }
	}

//	@GetMapping("/mypage/change-pass")
//	public String showChangPassForm (Model model) {
//		return "pass_form";
//	}
//
//	@GetMapping("/mypage/change-nickname")
//	public String showChangeNicknameForm (Model model) {
//		return "nickname_form";
//    }
}



