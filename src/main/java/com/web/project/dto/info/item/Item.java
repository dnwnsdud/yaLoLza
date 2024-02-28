package com.web.project.dto.info.item;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Item {
	public String id;
	public String name;
	public String description;
	public String plaintext;
	public ItemImage image;
	public Gold gold;
}
