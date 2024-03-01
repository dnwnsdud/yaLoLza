package com.web.project.dto.sjw.summoner;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="Summonermatchs")
@SequenceGenerator(
		name ="seq_Summonermatchs",
		sequenceName = "seq_Summonermatchs",
		initialValue = 100000,
		allocationSize = 1
		)	
public class Summonermatchs {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator= "seq_Summonermatchs")
    private Long id;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "summoner_id")
    private Summoner summoner;
    
    private String matchnum;
    
    private Long queueid;

     
    
}
