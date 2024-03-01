package com.web.project.dto.sjw.match;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name="Metadata")
@SequenceGenerator(
		name ="seq_metadata", //연결 이름
		sequenceName = "seq_metadata",
		initialValue = 100000,
		allocationSize = 1
		
		)
public class Metadata {
	@Id
	@GeneratedValue(generator= "seq_metadata",strategy = GenerationType.SEQUENCE)
	private Long id;
	private String dataVersion;
	private String matchId;
	
	//@Column(length = 100, columnDefinition = "nvarchar2(80)")
	private List<String> participants;
	
	@OneToOne
	@JoinColumn(name="metadata_id")
	private Match match;
}
