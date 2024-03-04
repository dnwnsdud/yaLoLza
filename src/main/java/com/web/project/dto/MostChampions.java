package com.web.project.dto;

import java.util.Date;
import java.util.List;

import com.web.project.dto.enumerated.UserRole;
import com.web.project.dto.sjw.match.Info;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MostChampions")
@SequenceGenerator(
        name = "seq_MostChampions",
        sequenceName = "seq_MostChampions",
        initialValue = 100000,
        allocationSize = 1
)
public class MostChampions {
   
  	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_MostChampions")
    private Long id;
	 
  	 @ManyToOne
     @JoinColumn(name = "duo_id") // 수정: 테이블에 맞게 칼럼명 변경
     private Duo duo;
	 
	 String name;
	 Long round;
	 Long wins;
	 Double kills;
	 Double deaths;
	 Double assists;
	 Double kda;
	 Double  Contribution;
	
}
