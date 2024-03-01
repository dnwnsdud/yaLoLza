package com.web.project.dto.sjw.TimeLine;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Data
public class Info {
    private Long frameInterval = 60000L;
    
    private List<Frames> frames;
    private Long gameId;
    private List<ParticipantFrames> participantFrames;
    
	
}
