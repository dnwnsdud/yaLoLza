package com.web.project.dto.sjw.TimeLine;

import java.util.Set;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParticipantInfo {
	private ChampionStats championStats;
	private Long currentGold;
	private DamageStats damageStats;
	private Long goldPerSecond;
	private Long jungleMinionsKilled;
	private Long level;
	private Long minionsKilled;
	private Long participantId;
	private Set<Position> position;
	private Long timeEnemySpentControlled;
	private Long totalGold;
	private Long xp;

}
