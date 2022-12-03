package com.dsg.wardstudy.domain.comment.service;

import com.dsg.wardstudy.domain.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(Long studyGroupId, CommentDto commentDto);

    List<CommentDto> getCommentsByStudyGroupId(Long studyGroupId);

    CommentDto getCommentById(Long studyGroupId, Long commentId);
}
