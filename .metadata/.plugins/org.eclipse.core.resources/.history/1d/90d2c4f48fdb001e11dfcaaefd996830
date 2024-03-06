package com.web.project.api.controller;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.project.dao.UserRepository;
import com.web.project.dto.MailDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class SendEmailService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "0503kkyymm@naver.com";
    private final PasswordEncoder passwordEncoder;
    SendEmailService sendEmailService;

    public MailDTO createMailAndChargePassword(String userEmail, String userName){
        String str = getTempPassword();
        MailDTO dto = new MailDTO();
        dto.setAddress(userEmail);
        dto.setTitle("야롤자 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. 야롤자 임시 비밀번호 안내 관련 이메일 입니다." + "회원님의 임시 비밀번호는 [ " + str + " ] 입니다." + "로그인 후에 비밀번호를 변경을 해주세요.");
        updatePassword(str, userEmail);
        return dto;
    }

    // 임시 비밀번호 업데이트
    public void updatePassword (String str, String userEmail){
        String pw = passwordEncoder.encode(str);
        Long id = userRepository.findByEmail(userEmail).getId();
        userRepository.updatePw(pw,id);
    }

    // 임시 비밀번호 생성
    public String getTempPassword(){
        char[] charset = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
        'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String str = "";

        int idx = 0; 
        for(int i=0; i<10; i++){
            idx = (int) (charset.length * Math.random());
            str += charset[idx];
        }
        return str;
    }

    // 메일 보내기
    public void mailSend (MailDTO mailDTO){
        System.out.println("이메일 전송함");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getAddress());
        message.setSubject(SendEmailService.FROM_ADDRESS);
        message.setText(mailDTO.getMessage());
        message.setFrom("0503kkyymm@naver.com");
        message.setReplyTo("0503kkyymm@naver.com");
        System.out.println("message" + message);
        mailSender.send(message);
    }

}
