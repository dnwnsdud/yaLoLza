package com.web.project.dto.sjw.TimeLine;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Events {
 private String type;
 private Long itemId;
 private Long skillSlot;
 private Long participantId;
 private Long timestamp;
}
