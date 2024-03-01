package com.web.project.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.web.project.dto.enumerated.CommunityEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "community")
@SequenceGenerator(
        name = "seq",
        sequenceName = "seq_community",
        initialValue = 100000,
        allocationSize = 1
)
public class Community {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq")
	    private Integer id;
		@Column(nullable = false)
	    private String title;
		@Column(nullable = false, length = 10000)
	    private String content;

	    private LocalDateTime createDate;
//	    private String createDate;
	//
//	    public void setCreateDate(LocalDateTime createAt) {
//	        this.createDate = Time.timesAgo(createAt);
//	    }



	    private String category;

	    private String filename;

	    @ElementCollection
	    private List<String> filePath;

	    @OneToMany(mappedBy = "community", cascade = CascadeType.REMOVE)
	    private List<Comment> commentList;

	    //    조회수
	    @Column(columnDefinition = "integer default 0", nullable = false)
	    private int writeview;
	    public CommunityEnum getCategoryEnum() {
	        switch (this.category) {
	            case "free":
	                return CommunityEnum.FREE;
	            case "humer":
	                return CommunityEnum.HUMER;
	            case "video":
	                return CommunityEnum.VIDEO;
	            default:
	                throw new RuntimeException("올바르지않습니다.");
	        }
	    }

	    @ManyToOne
	    private SiteUser siteUser;

}
