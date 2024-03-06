package com.web.project.dto.sjw.match;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
import jakarta.persistence.Lob;
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
@Table(name = "Match")
@SequenceGenerator(
        name = "seq_Match",
        sequenceName = "seq_Match",
        initialValue = 100000,
        allocationSize = 1
)
public class Match {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_Match")
    private Long id;
    
    private String matchnumber;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "match")  
    private Metadata metadata;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "match")
    private Info info;
    
    @Lob
    private String jsonData;
  

    public String getJsonData() {
        return jsonData;
    }
    
    private String caludate = "d";
    
    private Long maxdmg =0L;
    
    private Long maxdmged =0L;

    public void updateCaludate() {
        Instant creationInstant = Instant.ofEpochMilli(info.getGameCreation());
        LocalDateTime creationTime = LocalDateTime.ofInstant(creationInstant, ZoneOffset.UTC);
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        long minutesDiff = java.time.Duration.between(creationTime, now).toMinutes(); // 분 단위의 차이 계산

        if (minutesDiff < 60) {
            caludate = minutesDiff + "분 전";
        } else {
            long hoursDiff = minutesDiff / 60;
            if (hoursDiff < 24) {
                caludate = hoursDiff + "시간 전";
            } else if (24 <= hoursDiff && hoursDiff < 720) {
                long daysDiff = hoursDiff / 24;
                if (daysDiff <= 1) {
                    caludate = daysDiff + "하루 전";
                } else {
                    caludate = daysDiff + "일 전";
                }
            } else if (hoursDiff >= 720) {
                long monthsDiff = hoursDiff / 720;
                caludate = monthsDiff + "달 전";
            }
        }
    } 
        
        public void teammaxdmg() {
 
        	long  r = 0;
         for(int i=0 ; i< info.getParticipants().size() ; i ++){ 
        	  long  p = info.getParticipants().get(i).getTotalDamageDealtToChampions(); 
        
         	   if(p > r) {
         		   r = p;
         	   }
         	  this.maxdmg = r;
         	   }
         
         
        }
         
        
        public void teammaxdfs() {
           	long  r = 0;
            for(int i=0 ; i< info.getParticipants().size() ; i ++){ 
           	  long  p = info.getParticipants().get(i).getTotalDamageTaken(); 
           
            	   if(p > r) {
            		   r = p;
            	   }
            	   this.maxdmged = r;
            	   }
            }
        
        
      
       
       
       
    }
    
