package com.web.project.dto.TimeLine;

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
public class DamageStats extends ParticipantInfo{
    private Long magicDamageDone;
    private Long magicDamageDoneToChampions;
    private Long magicDamageTaken;
    private Long physicalDamageDone;
    private Long physicalDamageDoneToChampions;
    private Long physicalDamageTaken;
    private Long totalDamageDone;
    private Long totalDamageDoneToChampions;
    private Long totalDamageTaken;
    private Long trueDamageDone;
    private Long trueDamageDoneToChampions;
    private Long trueDamageTaken;
}