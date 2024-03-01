package com.web.project.dto.sjw.TimeLine;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParticipantFrames {
    private List<ParticipantNum> participantNum;
	/*
	 * private ParticipantInfo participantInfo; private Map<Long, ParticipantInfo>
	 * participantFrames;
	 */
   private Long  timestamp;
}
