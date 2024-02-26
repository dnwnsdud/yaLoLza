package com.web.project.dto.Champion;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Passive {
	public String name;
	public String description;
	public PassiveImage image;
}
