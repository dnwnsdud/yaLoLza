package com.web.project.dto.item;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Gold {
	public Long base;
	public Long total;
	public Long sell;
}
