package com.web.project.dto.sjw.summoner;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.web.project.dto.sjw.match.Info;
import com.web.project.dto.sjw.match.Match;
import com.web.project.dto.sjw.match.Participants;
import com.web.project.dto.sjw.match.team.Team;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Lazy
@Scope("prototype")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="Summoner")
@SequenceGenerator(
		name ="seq_Summoner",
		sequenceName = "seq_Summoner",
		initialValue = 100000,
		allocationSize = 1
		)
public class Summoner {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator= "seq_Summoner")
     private Long id;
    
   

     private Long summonerLevel;
     private Long  profileIconId;
     private String summonername;
     private String tag;     
     private String recenttier;
     private Long leaguePoints;
     private Long wins;
     private Long losses;
     private String rank;
	
	 @OneToMany(cascade = CascadeType.ALL,mappedBy = "summoner")
	 private List<Summonertierlog> tierlog;
	 
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "summoner")
	private List<Summonermatchs> matchs;
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "summoner")
	private List<Summonerchampions> champions;
     
    
    public String toString() {
        return "Summoner{" +
                "id=" + id +
                ", summonername='" + summonername + '\'' +
                ", tag='" + tag + '\'' +
                ", champions=" + champions +
                '}';
    }
}