package com.web.project.dto.info.Champion;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Spell {
	String id;
	String name;
	String description;
	String tooltip;
	String cooldownBurn;
	String costBurn;
	String rangeBurn;
	String key;
}
