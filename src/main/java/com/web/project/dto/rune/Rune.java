package com.web.project.dto.rune;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Rune {
	Integer id;
	String key;
	String icon;
	String name;
	String longDesc;
}
