package com.web.project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordForm {

    private Long id;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;

}
