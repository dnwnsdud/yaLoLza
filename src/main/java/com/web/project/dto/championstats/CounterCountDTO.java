package com.web.project.dto.championstats;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CounterCountDTO {
    private int count;

    private List<CounterChampionDTO> counter;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<CounterChampionDTO> getCounter() {
		return counter;
	}

	public void setCounter(List<CounterChampionDTO> counter) {
		this.counter = counter;
	}


}