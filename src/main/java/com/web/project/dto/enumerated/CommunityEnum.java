package com.web.project.dto.enumerated;

import lombok.Getter;

@Getter
public enum CommunityEnum {
	All("전체"),
    FREE("자유"),
    HUMER("유머"),
    VIDEO("영상"),
    BEST("인기");

    private String group;

    CommunityEnum(String group) {
        this.group = group;
    }
}
