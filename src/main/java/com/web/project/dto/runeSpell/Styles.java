package com.web.project.dto.runeSpell;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

public class Styles {

	
	private String description;
	private List<Selections> selections;
	private int style;
	
	public String getDescription() {
		return description;
	}
	public List<Selections> getSelections(){
		return selections;
	}
	public int getStyle() {
		return style;
	}
	
	
}
