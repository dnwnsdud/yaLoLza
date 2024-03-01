
  package com.web.project.dto.sjw.match.team;
  
  import java.util.List;

import org.springframework.stereotype.Component;

import com.web.project.dto.sjw.match.Info;

import jakarta.persistence.CascadeType; import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
  
  @Component
  @Data
  @Entity
  @NoArgsConstructor
  @AllArgsConstructor
  @Table(name="Team")
  @SequenceGenerator(
			name ="seq_Team", //연결 이름
			sequenceName = "seq_Team",
			initialValue = 100000,
			allocationSize = 1
			)
  public class Team {
	  @Id
	  @GeneratedValue(strategy = GenerationType.SEQUENCE,generator= "seq_Team") // 또는 다른 적절한 전략 선택
	  private Long Id;
	  
	  
	  @ManyToOne(cascade = {CascadeType.ALL})
	  @JoinColumn(name = "teams_id")
	  private Info info;
 
//	  @OneToMany(cascade = {CascadeType.ALL})
//	  private List<Ban>bans;
	  
	  @OneToOne(cascade = CascadeType.ALL, mappedBy = "team")
	  private Objectives objectives;
	  
	  private Long teamId;
	  
	  private Boolean win; 
	  
	  private Long maxdemage;
	  
	  private Long maxdefense;
	  
  }

