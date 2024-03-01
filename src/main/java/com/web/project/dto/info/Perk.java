package com.web.project.dto.info;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perk {
	Integer id;
	String name;
	String tooltip;
	String group;
	String icon;
}
