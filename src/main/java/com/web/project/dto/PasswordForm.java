package com.web.project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordForm {

    private Long id;

    
    private String password;

}
