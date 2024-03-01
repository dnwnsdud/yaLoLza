package com.web.project.dto.sjw.match;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.web.project.dto.sjw.match.perks.Perks;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Participants")
@SequenceGenerator(
        name = "seq_Participants",
        sequenceName = "seq_Participants",
        initialValue = 100000,
        allocationSize = 1
)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Participants {
	 @Id
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_Participants")
	 private Long id;
	 
		@ManyToOne(cascade = {CascadeType.ALL})
		@JoinColumn(name="participants_id")
		private Info info;
	
     @Transient
	 private Perks perks;	
	
	 @Column(name="allPings")
	 private Long allInPings;

	 @Column(name="assistPings")
	 private Long assistMePings;

	 @Column(name="assists")
	 private Long assists;

	 @Column(name="baronKills")
	 private Long baronKills;

	 @Column(name="basicPings")
	 private Long basicPings;

	 @Column(name="bountyLvl")
	 private Long bountyLevel;

	 @Column(name="champLvl")
	 private Long champLevel;

	 @Column(name="champId")
	 private Long championId;

	 @Column(name="champName")
	 private String championName;

	 @Column(name="champTransform")
	 private Long championTransform;

	 @Column(name="cmdPings")
	 private Long commandPings;

	 @Column(name="Purchased")
	 private Long consumablesPurchased;

	 @Column(name="dmgToBuildings")
	 private Long damageDealtToBuildings;

	 @Column(name="dmgToObj")
	 private Long damageDealtToObjectives;

	 @Column(name="dmgToTurrets")
	 private Long damageDealtToTurrets;

	 @Column(name="dmgSelfMitigated")
	 private Long damageSelfMitigated;
	  
	 @Column(name="dangerPings")
	    private Long dangerPings;

	    @Column(name="deaths")
	    private Long deaths;

	    @Column(name="detectorWardsPlaced")
	    private Long detectorWardsPlaced;

	    @Column(name="doubleKills")
	    private Long doubleKills;

	    @Column(name="dragonKills")
	    private Long dragonKills;

	    @Column(name="eligible")
	    private Boolean eligibleForProgression;

	    @Column(name="enemyMissingPings")
	    private Long enemyMissingPings;

	    @Column(name="enemyVisionPings")
	    private Long enemyVisionPings;

	    @Column(name="firstBloodAssist")
	    private Boolean firstBloodAssist;

	    @Column(name="firstBloodKill")
	    private Boolean firstBloodKill;

	    @Column(name="firstTowerAssist")
	    private Boolean firstTowerAssist;

	    @Column(name="firstTowerKill")
	    private Boolean firstTowerKill;

	    @Column(name="earlySurrender")
	    private Boolean gameEndedInEarlySurrender;

	    @Column(name="surrender")
	    private Boolean gameEndedInSurrender;

	    @Column(name="getBackPings")
	    private Long getBackPings;

	    @Column(name="goldEarned")
	    private Long goldEarned;

	    @Column(name="goldSpent")
	    private Long goldSpent;

	    @Column(name="holdPings")
	    private Long holdPings;

	    @Column(name="position")
	    private String individualPosition;;

	    @Column(name="IKills")
	    private Long inhibitorKills;

	    @Column(name="ITakedowns")
	    private Long inhibitorTakedowns;

	    @Column(name="ILost")
	    private Long inhibitorsLost;

	    @Column(name="item0")
	    private Long item0;

	    @Column(name="item1")
	    private Long item1;

	    @Column(name="item2")
	    private Long item2;

	    @Column(name="item3")
	    private Long item3;

	    @Column(name="item4")
	    private Long item4;

	    @Column(name="item5")
	    private Long item5;

	    @Column(name="item6")
	    private Long item6;

	    @Column(name="itemsPurchased")
	    private Long itemsPurchased;

	    @Column(name="killingSprees")
	    private Long killingSprees;

	    @Column(name="kills")
	    private Long kills;

	    @Column(name="lane")
	    private String lane;

	    @Column(name="LCriticalStrike")
	    private Long largestCriticalStrike;

	    @Column(name="LKillingSpree")
	    
	    private Long largestKillingSpree;

	    @Column(name="LMultiKill")
	    private Long largestMultiKill;

	    @Column(name="LTimeSpentLiving")
	    private Long longestTimeSpentLiving;

	    @Column(name="mDDealt")
	    private Long magicDamageDealt;

	    @Column(name="mDDChampions")
	    private Long magicDamageDealtToChampions;

	    @Column(name="mDmgTaken")
	    private Long magicDamageTaken;

	    @Column(name="needVisionPings")
	    private Long needVisionPings;

	    @Column(name="minionsKilled")
	    private Long neutralMinionsKilled;

	    @Column(name="nexusKills")
	    private Long nexusKills;

	    @Column(name="nexusLost")
	    private Long nexusLost;

	    @Column(name="nexusTakedowns")
	    private Long nexusTakedowns;

	    @Column(name="objStolen")
	    private Long objectivesStolen;

	    @Column(name="objStolenAssists")
	    private Long objectivesStolenAssists;

	    @Column(name="onMyWayPings")
	    private Long onMyWayPings;

	    @Column(name="participantId")
	    private Long participantId;

	    @Column(name="pentaKills")
	    private Long pentaKills;

	    @Column(name="pDmgDealt")
	    private Long physicalDamageDealt;

	    @Column(name="pDDChampions")
	    private Long physicalDamageDealtToChampions;

	    @Column(name="pDmgTaken")
	    private Long physicalDamageTaken;

	    @Column(name="profileIcon")
	    private Long profileIcon;

	    @Column(name="pushPings")
	    private Long pushPings;

	    
	    @Column(name="puuid")
	    private String puuid;

	    @Column(name="quadraKills")
	    private Long quadraKills;

	    @Column(name="gameName")
	    private String riotIdGameName;

	    @Column(name="tagline")
	    private String riotIdTagline;

	    @Column(name="role")
	    private String role;

	    @Column(name="WardBoughtInGame")
	    private Long sightWardsBoughtInGame;

	    @Column(name="spell1Casts")
	    private Long spell1Casts;

	    @Column(name="spell2Casts")
	    private Long spell2Casts;

	    @Column(name="spell3Casts")
	    private Long spell3Casts;

	    @Column(name="spell4Casts")
	    private Long spell4Casts;

	    @Column(name="subteamPlacement")
	    private Long subteamPlacement;

	    @Column(name="summoner1Casts")
	    private Long summoner1Casts;

	    @Column(name="summoner1Id")
	    private Long summoner1Id;
	    
	    @Column(name="summoner2Casts")
	    private Long summoner2Casts;
	    
	    @Column(name="summoner2Id")
	    private Long summoner2Id;
	    
	    @Column(name="summonerLevel")
	    private Long summonerLevel;

	    @Column(name="summonerName")
	    private String summonerName;

	    @Column(name="earlySurrendered")
	    private Boolean teamEarlySurrendered;

	    @Column(name="teamId")
	    private Long teamId;

	    @Column(name="teamPosition")
	    private String teamPosition;

	    @Column(name="timeCCingOthers")
	    private Long timeCCingOthers;

	    @Column(name="timePlayed")
	    private Long timePlayed;

	    @Column(name="TJleMinionsKilled")
	    private Long totalAllyJungleMinionsKilled;

	    @Column(name="TDamageDealt")
	    private Long totalDamageDealt;

	    @Column(name="TDDealtToChampions")
	    private Long totalDamageDealtToChampions;

	    @Column(name="TDShieldTeammates")
	    private Long totalDamageShieldedOnTeammates;

	    @Column(name="TDTaken")
	    private Long totalDamageTaken;

	    @Column(name="TEJungleMinionKill")
	    private Long totalEnemyJungleMinionsKilled;

	    @Column(name="totalHeal")
	    private Long totalHeal;

	    @Column(name="THOnTeammates")
	    private Long totalHealsOnTeammates;

	    @Column(name="TMinionsKilled")
	    private Long totalMinionsKilled;

	    @Column(name="TTimeCCDealt")
	    private Long totalTimeCCDealt;

	    @Column(name="TTimeSpentDead")
	    private Long totalTimeSpentDead;

	    @Column(name="TUnitsHealed")
	    private Long totalUnitsHealed;

	    @Column(name="tripleKills")
	    private Long tripleKills;

	    @Column(name="trueDamageDealt")
	    private Long trueDamageDealt;

	    @Column(name="TDDChampions")
	    private Long trueDamageDealtToChampions;

	    @Column(name="trueDamageTaken")
	    private Long trueDamageTaken;

	    @Column(name="turretKills")
	    private Long turretKills;

	    @Column(name="turretTakedowns")
	    private Long turretTakedowns;

	    @Column(name="turretsLost")
	    private Long turretsLost;

	    @Column(name="unrealKills")
	    private Long unrealKills;

	    @Column(name="ClearedPings")
	    private Long visionClearedPings;

	    @Column(name="visionScore")
	    private Long visionScore;

	    @Column(name="WardsBought")
	    private Long visionWardsBoughtInGame;

	    @Column(name="wardsKilled")
	    private Long wardsKilled;

	    @Column(name="wardsPlaced")
	    private Long wardsPlaced;

	    @Column(name="win")
	    private Boolean win;
	    
	    
	
	    }
