package com.web.project.dto.match;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.web.project.dto.match.team.Team;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
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
@Table(name="info")
@SequenceGenerator(
		name ="seq_infos", //연결 이름
		sequenceName = "seq_infos",
		initialValue = 100000,
		allocationSize = 1
		)
public class Info{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator= "seq_infos") // seq_users 연결 이름
    Long id;
	
	
	
	private Long gameCreation;
	private Long gameDuration;
	private Long gameEndTimestamp;
	private Long gameId;
	
   //  @Enumerated(EnumType.STRING) 
	private String gameMode;
	private String gameName;
	private Long gameStartTimestamp;
	private String gameType;
	private String gameVersion;
	private Long mapId;
	private String platformId;
	private Long queueId;
	@OneToMany(cascade = {CascadeType.ALL},mappedBy = "info")
	private List<Participants> participants;
	
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "info")
	 private List<Team> teams;
	
	@OneToOne
	@JoinColumn(name="info_id")
	private Match match;
	

   
	
}