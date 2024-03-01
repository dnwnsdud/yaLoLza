package com.web.project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuopasswordForm {
	 
//	 private Long id;
	 
	@NotEmpty(message = "비밀번호는 필수항목입니다.")
	 private String password1;
	@NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
	 private String password2;

}
