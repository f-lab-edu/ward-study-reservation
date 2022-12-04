package com.dsg.wardstudy.domain.comment.service;

import com.dsg.wardstudy.domain.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(Long studyGroupId, Long userId, CommentDto commentDto);

    List<CommentDto> getCommentsByStudyGroupId(Long studyGroupId);

    CommentDto getCommentById(Long studyGroupId, Long userId, Long commentId);

    CommentDto updateComment(Long studyGroupId, Long userId, Long commentId, CommentDto commentDto);

    void deleteComment(Long studyGroupId, Long userId, Long commentId);
}
