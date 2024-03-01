package com.web.project.dto.info;

import java.util.List;

import org.springframework.stereotype.Component;

import com.web.project.dto.info.rune.Slots;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Runes {
	Integer id;
	String key;
	String icon;
	String name;
	String longDesc;
	List<Slots> slots;
}
