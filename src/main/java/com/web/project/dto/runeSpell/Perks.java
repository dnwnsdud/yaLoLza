package com.web.project.dto.runeSpell;

import java.util.Collection;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;


public class Perks {

	private StatPerks statPerks;
    private List<Styles> styles;
    
    
	public StatPerks getStatPerks() {
		return statPerks;
		}


	public List<Styles> getStyles() {	
		return styles;
	}
	
}