package com.web.project.page.controller;

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

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
    	new Connect("total","yalolza.gg", "user");
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
        return "login_form";
    }

    @GetMapping("/login")
    public String login() {
    	new Connect("total","yalolza.gg", "user");
        return "login_form";
    }


//    @GetMapping("/delete")
//    public String deleteUser() {
//        return "home";
//    }

    @GetMapping("/unregist")
    public String userDelete(@AuthenticationPrincipal SiteUser user, Model model, PasswordForm passwordForm) {
        model.addAttribute("user", user);
    	new Connect("total","yalolza.gg", "user");
        return "mypage_form";
    }

    @PostMapping("/unregist")
    public String userDelete(@Valid PasswordForm passwordForm, BindingResult bindingResult, SiteUser user, Long id) {
        if (bindingResult.hasErrors()) {
            return "mypage_form";
        }
        userService.deleteUser(id);
        Counter.Decrement("userCount", 1);
        return "redirect:/yalolzq.gg/user/logout";

    }

    @GetMapping("/mypage")
    public String userMypage(@AuthenticationPrincipal SiteUser user, Model model) {
        model.addAttribute("user", user);
    	new Connect("total","yalolza.gg", "user");
        return "mypage_form";
    }
    
   
}



