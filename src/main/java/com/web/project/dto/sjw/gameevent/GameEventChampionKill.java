package com.web.project.dto.sjw.gameevent;

import java.util.List;

import com.web.project.dto.sjw.gameevent.sub.Position;
import com.web.project.dto.sjw.gameevent.sub.VictimDamage;

import lombok.Data;

@Data
public class GameEventChampionKill extends GameEvent{
	List<Integer> assistingparicipantids;
	Integer bounty;
	Integer killstreaklength;
	Integer killerid;
	Position position;
	Integer shutdownbounty;
	List<VictimDamage> victimdamagedealt;
	List<VictimDamage> victimdamagereceived;
	Integer victimid;
}
