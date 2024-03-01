package com.web.project.dto.sjw.rankedinfo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="RankedInfo")
@SequenceGenerator(
		name ="seq_RankedInfo", //연결 이름
		sequenceName = "seq_RankedInfo",
		initialValue = 100000,
		allocationSize = 1
		)
public class RankedInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator= "seq_RankedInfo") // 또는 다른 적절한 전략 선택
    private Long id; // 식별자 필드 추가

    private String leagueId;
    private String queueType;
    private String tier;
    private String rank;
    private String summonerId;
    private String summonerName;
    private Long leaguePoints;
    private Long wins;
    private Long losses;
    private Boolean veteran;
    private Boolean inactive;
    private Boolean freshBlood;
    private Boolean hotStreak;
}
