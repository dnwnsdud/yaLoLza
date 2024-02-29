
  package com.web.project.dto.match.team;
  


import org.hibernate.annotations.JavaType;
import org.hibernate.annotations.JdbcType;
import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
  @Table(name="Objectives")
  @SequenceGenerator(
			name ="seq_Objectives", //연결 이름
			sequenceName = "seq_Objectives",
			initialValue = 100000,
			allocationSize = 1
			)
  public class Objectives {

		   @Id
		   @GeneratedValue(strategy = GenerationType.SEQUENCE,generator= "seq_Objectives")
           private Long id;
		   
		  @OneToOne(cascade = CascadeType.ALL)
		  @JoinColumn(name = "objectives_id")
		  private Team team;
          
		  @OneToOne(cascade = CascadeType.ALL, mappedBy = "objectives")
		  private Objective baron;
		  
		  @OneToOne(cascade = CascadeType.ALL, mappedBy = "objectives")
		  private Objective champion;
		  
		  @OneToOne(cascade = CascadeType.ALL, mappedBy = "objectives")
		  private Objective dragon;
		  
		  @OneToOne(cascade = CascadeType.ALL, mappedBy = "objectives")
		  private Objective horde;
		  
		  @OneToOne(cascade = CascadeType.ALL, mappedBy = "objectives")
		  private Objective inhibitor;
		  
		  @OneToOne(cascade = CascadeType.ALL, mappedBy = "objectives")
		  private Objective riftHerald;
		  
		  @OneToOne(cascade = CascadeType.ALL, mappedBy = "objectives")
		  private Objective tower;
  
  }
 