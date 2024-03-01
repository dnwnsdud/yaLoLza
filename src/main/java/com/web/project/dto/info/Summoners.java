package com.web.project.dto.info;

import java.util.List;

import org.springframework.stereotype.Component;

import com.web.project.dto.info.Champion.Spell;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Summoners {
	String type;
	String version;
	List<Spell> data;
}
