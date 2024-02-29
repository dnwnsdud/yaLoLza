package com.web.project.dto.summoner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.persistence.*;

@Component
@Entity
@Scope("prototype")
@Table(name = "SummonerChampions")
@SequenceGenerator(
    name = "seq_SummonerChampions",
    sequenceName = "seq_SummonerChampions",
    initialValue = 100000,
    allocationSize = 1
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Summonerchampions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_SummonerChampions")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "summoner_id")
    private Summoner summoner;

    private String position; // 포지션
    private String champion; // 챔피언
    private String enemychampion; // 챔피언
    private Double csaverage; // cs 평균
    private Double championkda; // 챔피언 KDA
    private Long damagedealt; // 가한 피해량
    private Long damagetaken; // 받은 피해량
    private Long queueId; // 큐타입
    private Integer win; // 승패
    private Long championkills; //킬
    private Long championdeaths;//데스
    private Long championassists;//어시
    private Long killpers;//킬관여율
    
    @Override
    public String toString() {
        return "Summonerchampions{" +
                "id=" + id +
                ", summoner=" + (summoner != null ? summoner.getId() : null) +
                ", position='" + position + '\'' +
                ", champion='" + champion + '\'' +
                ", enemychampion='" + enemychampion + '\'' +
                ", csAverage=" + csaverage +
                ", championKDA=" + championkda +
                ", damageDealt=" + damagedealt +
                ", damageTaken=" + damagetaken +
                ", queueId=" + queueId +
                ", win=" + win +
                '}';
    }
}