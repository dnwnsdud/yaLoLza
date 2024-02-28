package com.web.project.dto.info.Champion;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class ChampionImage {
	public String full;
	public String group;
}
