package com.web.project.dto.Champion;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class Skin {
	String id;
	Integer num;
	String name;
	boolean chromas;
}
