package com.web.project.api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.project.Exception.DataNotFoundException;
import com.web.project.dao.CommentRepository;
import com.web.project.dto.Comment;
import com.web.project.dto.Community;
import com.web.project.dto.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public void commentcreate(Community community, String content, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setCommunity(community);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
    }

    public void  delete(Comment comment) {
        this.commentRepository.delete(comment);
    }

    public Comment getComment(Integer id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if(comment.isPresent()){
            return comment.get();
        } else {
            throw new DataNotFoundException("comment not");
        }
    }
    
	public List<Comment> getAllComments() {
		return commentRepository.findAllByOrderByIdDesc();
	}
	public void deleteComment(Integer id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        commentOptional.ifPresent(commentRepository::delete);
      
    }

}

