package com.web.project.dto.info.Champion;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Stat {
	private Long hp;
	private Long hpperlevel;
	private Long mp;
	private Long mpperlevel;
	private Long movespeed;
	private Long armor;
	private Double armorperlevel;
	private Long spellblock;
	private Double spellblockperlevel;
	private Long attackrange;
	private Double hpregen;
	private Double hpregenperlevel;
	private Double mpregen;
	private Double mpregenperlevel;
	private Long crit;
	private Long critperlevel;
	private Long attackdamage;
	private Double attackdamageperlevel;
	private Double attackspeed;
	private Double attackspeedperlevel;
}
