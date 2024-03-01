package com.web.project.dto.info.Champion;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Champion {
	public String id;
	public String name;
	public String key;
	public ChampionImage image;
//	public List<Tag> tags;
	public List<String> tags;
	public String partype;
	public Stat stats;
	public List<Skin> skins;
	public List<Spell> spells;
	public Passive passive;
}
