
  package com.web.project.dto.match.team;
  
  import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import
  lombok.Data;
import lombok.NoArgsConstructor;
  
@Component
  @Data 
  @Entity 
  @NoArgsConstructor
  @AllArgsConstructor
  @Table(name="Objective")
  @SequenceGenerator(
	        name ="seq_Objective",
	        sequenceName = "seq_Objective",
	        initialValue = 100000,
	        allocationSize = 1
	)
  public class Objective {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "seq_Objective")
	    Long id;
	    private String first;
        private Long kills;
        
        @OneToOne
        @JoinColumn(name = "objectives_id")
        private Objectives objectives;

      
        
  }
 