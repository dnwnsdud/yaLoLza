package com.web.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.project.dto.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

}
