package com.web.project.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
	
	public class CounterPositionDTO {
	    @JsonProperty("TOP")
	    private List<CounterCountDTO> top;

	    @JsonProperty("JUNGLE")
	    private List<CounterCountDTO> jungle;

	    @JsonProperty("MIDDLE")
	    private List<CounterCountDTO> middle;

	    @JsonProperty("BOTTOM")
	    private List<CounterCountDTO> bottom;

	    @JsonProperty("UTILITY")
	    private List<CounterCountDTO> utility;

	    public List<CounterCountDTO> getTop() {
	        return top;
	    }

	    public List<CounterCountDTO> getJungle() {
	        return jungle;
	    }

	    public List<CounterCountDTO> getMiddle() {
	        return middle;
	    }

	    public List<CounterCountDTO> getBottom() {
	        return bottom;
	    }

	    public List<CounterCountDTO> getUtility() {
	        return utility;
	    }
}