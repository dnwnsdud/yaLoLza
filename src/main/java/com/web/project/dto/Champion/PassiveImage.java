package com.web.project.dto.Champion;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class PassiveImage {
	private String full;
	private String group;
}
