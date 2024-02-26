package com.web.project.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.web.project.dto.Champion.Spell;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Summoner {
	String type;
	String version;
	List<Spell> data;
}
