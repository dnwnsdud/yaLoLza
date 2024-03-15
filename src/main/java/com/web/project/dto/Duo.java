package com.web.project.dto;

import java.util.Date;
import java.util.List;

import com.web.project.dto.enumerated.Myposition;
import com.web.project.dto.enumerated.Queuetype;
import com.web.project.dto.enumerated.Yourposition;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "findduo")
@SequenceGenerator(
	name = "seq",
	sequenceName = "seq_findduo",
	initialValue = 100000,
	allocationSize = 1
)
public class Duo {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	Long id;
	
	@Column(nullable = false)
	String summonerName;
	@Column(nullable = false)
	@Digits(integer=4, fraction=0)
	String duopassword1;
	@Column(nullable = false)
	@Digits(integer=4, fraction=0)
	String duopassword2;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	Myposition myposition; // enum
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@ElementCollection
	List<Yourposition> yourposition; // enum
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	Queuetype queuetype; // enum
	@Column
	Boolean ismike;
	@Column(nullable = false)
	String memo;
	@Column
	String tier;
	// api
	  @OneToMany(mappedBy = "duo", cascade = CascadeType.ALL)
	   private List<MostChampions> mostChampions;
	
	private Date createdDate;
    private Date lastModifiedDate;
    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
        lastModifiedDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedDate = new Date();
    }
	

    @ManyToOne
	@JoinColumn(name = "site_user_id")
	private SiteUser siteUser;
	
}
