package com.web.project.dto.info.rune;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;
@Component
@Data
@NoArgsConstructor
public class Slots {
	List<Rune> runes;
}
