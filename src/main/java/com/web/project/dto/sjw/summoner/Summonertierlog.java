package com.web.project.dto.sjw.summoner;




import java.util.Date;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name="Summonertlog")
@SequenceGenerator(
		name ="seq_Summonertierlog",
		sequenceName = "seq_Summonertierlog",
		initialValue = 100000,
		allocationSize = 1
		)	
public class Summonertierlog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator= "seq_Summonertierlog")
    private Long id;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "summoner_id")
    private Summoner summoner;
    
    private String summonertier;
    private Long  point;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dates;
	 
    @PrePersist
    protected void onCreate() {
        dates = new Date();
    }

}