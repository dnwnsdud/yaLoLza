package com.web.project.dto.info.item;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class ItemImage {
	public String full;
	public String group;
}
