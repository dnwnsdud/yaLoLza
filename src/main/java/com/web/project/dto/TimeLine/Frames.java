package com.web.project.dto.TimeLine;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Data
public class Frames {
	private List<Events> events;
	private ParticipantFrames participantFrames;
	private Long timestamp;
	
	public List<Events> filterItemPurchasedEvents() {
        return events.stream()
                .filter(event -> "item_purchased".equalsIgnoreCase(event.getType()))
                .collect(Collectors.toList());
    }
	public List<Events> filterSkillLevelUpEvents() {
	    if (events == null || events.isEmpty()) {
	        return Collections.emptyList(); 
	    }
	    return events.stream()
	            .filter(event -> "SKILL_LEVEL_UP".equalsIgnoreCase(event.getType()))
	            .collect(Collectors.toList());
	}
}
