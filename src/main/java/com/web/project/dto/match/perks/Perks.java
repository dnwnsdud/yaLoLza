package com.web.project.dto.match.perks;

import java.util.List;

import org.springframework.stereotype.Component;

import com.web.project.dto.match.Info;
import com.web.project.dto.match.team.Objectives;
import com.web.project.dto.match.team.Team;
/*import com.web.thymeleaf.dto.MatchData;
import com.web.thymeleaf.dto.MatchData.MatchInfo.Participant.Perks.PerkSelection;
import com.web.thymeleaf.dto.MatchData.MatchInfo.Participant.Perks.PerkStyle;
import com.web.thymeleaf.dto.MatchData.MatchInfo.Participant.Perks.StatPerks;*/

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Perks {
	

	  
	  private StatPerks statPerks;
	  private  List<PerkStyle> styles;
	  
	  
	  @Data
	  public static class StatPerks {
		  private Long defense;
		  private Long flex;
		  private Long offense;
	}
	  
	  @Data 
	  public static class PerkStyle {
		  private String description;
		  private List<PerkSelection> selections;
		  private Long style; 
		  }
	  
	  @Data 
	  public static class PerkSelection { 
		  private Long perk;
		  private Long var1;
		  private Long var2;
		  private Long var3;
		  }
 
}
