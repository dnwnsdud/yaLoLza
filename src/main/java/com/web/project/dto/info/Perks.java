package com.web.project.dto.info;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Component
@NoArgsConstructor
public class Perks {
	List<ArrayList<Perk>> slots;
}
