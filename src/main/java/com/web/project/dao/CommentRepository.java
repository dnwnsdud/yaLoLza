package com.web.project.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.web.project.dto.Comment;
import com.web.project.dto.Community;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

	List<Comment> findAllByOrderByIdDesc();
	
	Page<Comment> findAllByCommunity(Community community, Pageable pageable);

}
