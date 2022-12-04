package com.dsg.wardstudy.domain.comment.repository;

import com.dsg.wardstudy.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByStudyGroupId(Long studyGroupId);
}
