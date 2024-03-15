package com.web.project.page.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.project.api.controller.SendEmailService;
import com.web.project.api.controller.UserService;
import com.web.project.dto.MailDTO;
import com.web.project.dto.PasswordForm;
import com.web.project.dto.SiteUser;
import com.web.project.dto.UserCreateForm;
import com.web.project.metrics.Counter;
import com.web.project.metrics.count.Connect;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/yalolza.gg/user")
public class UserPage {

    @Autowired
    PasswordEncoder encoder;
    private final UserService userService;

    @Autowired
	private final SendEmailService ms;
    
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
    	new Connect("total","yalolza.gg", "user","signup");
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

        Counter.Increment("userCount", 1);
        return "redirect:/yalolza.gg/user/login";
    }

    @GetMapping("/login")
    public String login() {
    	new Connect("total","yalolza.gg", "user","login");
        return "login_form";
    }

    @GetMapping("/unregist")
    public String userDelete(@AuthenticationPrincipal SiteUser user, Model model, @Valid PasswordForm passwordForm) {
        model.addAttribute("user", user);
        return "mypage_form";
    }

    @PostMapping("/unregist")
    public String userDelete(@Valid PasswordForm passwordForm, BindingResult bindingResult, SiteUser user, Long id) {
        if (bindingResult.hasErrors()) {
            return "mypage_form";
        }
        userService.deleteUser(id);
        Counter.Decrement("userCount", 1);
        return "redirect:/yalolza.gg/user/logout";

    }
    
    @GetMapping("/mypage")
    public String userMypage(@AuthenticationPrincipal SiteUser user, Model model) {
        model.addAttribute("user", user);
    	new Connect("total","yalolza.gg", "user","mypage");
        return "mypage_form";
    }

    @PostMapping("/mypage/change-username")
    public String changeUsername (@RequestParam("newNickname")  String newNickname, Model model, RedirectAttributes redirectAttributes) {
       boolean result = userService.changeNickname(newNickname);
       if(!result) {
        redirectAttributes.addFlashAttribute("nicknameError", "이미 존재하는 닉네임 입니다.");
        return "redirect:/yalolza.gg/user/mypage";
       }else{
        redirectAttributes.addFlashAttribute("nicknameChange", "닉네임 변경 성공했습니다. 재접속시 변경된 닉네임이 적용됩니다." );
       }
       return "redirect:/yalolza.gg/user/mypage";
    }

    @PostMapping("/mypage/change-pass")
   public String changeUserPass (@RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass, @RequestParam("newPassConfirm") String newPassConfirm, @AuthenticationPrincipal SiteUser currentUser, RedirectAttributes redirectAttributes) {
      Long userId = currentUser.getId();

      boolean result = userService.changePass(userId, oldPass, newPass, newPassConfirm);
      if(!result){
        redirectAttributes.addFlashAttribute("passwordError", "비밀번호 변경에 실패했습니다. 입력 정보를 확인해 주세요.");
        return "redirect:/yalolza.gg/user/mypage";
        }else{
            redirectAttributes.addFlashAttribute("passwordChange", "비밀번호 변경에 성공했습니다."); 
        }
        return "redirect:/yalolza.gg/user/mypage";
    }

 // Email + name 일치하는지
 	@GetMapping("/check/findPw")
 	public @ResponseBody Map<String,Boolean> pwFind (@RequestParam("userEmail") String userEmail, @RequestParam("userName") String userName) {
 		Map<String, Boolean> json = new HashMap<>();
 		boolean pwFindCheck = userService.userEmailCheck(userEmail, userName);
 		json.put("check", pwFindCheck);
 		return json;
 	}

 	// 등록된 이메일로 발송 + 비밀번호 임시 변경
 	@PostMapping("/check/findPw/sendEmail")
 	public @ResponseBody void sendEmail(String userEmail, String userName){
 		MailDTO dto = ms.createMailAndChargePassword(userEmail, userName);
 		ms.mailSend(dto);
 	}
 	
 	
 	// 비밀번호 찾기 페이지로 이동
 	@GetMapping("/FindPw_show")
 	public String FindPw_show() {
 		return "FindPw_form";
 	}
}

