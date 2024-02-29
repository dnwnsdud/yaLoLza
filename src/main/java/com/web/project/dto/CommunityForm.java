package com.web.project.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityForm {
//   vaild 를 사용하기 위해 만듬 제목이랑 내용을 입력안하면 경고창이 뜸
    @NotEmpty(message = "제목은 필수로 입력해주세요.")
    @Size(max=200)
    private String title;

    @NotEmpty(message = "내용 필수로 입력해주세요.")
    @Size(max=2000)
    private String content;


}