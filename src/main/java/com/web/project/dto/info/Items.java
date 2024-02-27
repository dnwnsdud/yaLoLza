package com.web.project.dto.info;

import java.util.List;

import org.springframework.stereotype.Component;

import com.web.project.dto.info.item.Item;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Items {
	public String type;
	public String version;
	public List<Item> data;
}
