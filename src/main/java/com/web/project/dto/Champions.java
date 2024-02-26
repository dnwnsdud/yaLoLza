package com.web.project.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.web.project.dto.Champion.Champion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Champions {
	public String type;
	public String version;
	public List<Champion> data;
}
