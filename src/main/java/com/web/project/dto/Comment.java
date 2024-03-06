package com.web.project.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name = "commentUsers")
@SequenceGenerator(
        name = "seq",
        sequenceName = "seq_comment",
        initialValue = 100000,
        allocationSize = 1
) 

public class Comment {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq")
    private Integer id;

	@Column(nullable = false)
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private Community community;

    @ManyToOne
    private SiteUser author;
}
