package com.dsg.wardstudy.domain.comment.service;

import com.dsg.wardstudy.common.exception.ErrorCode;
import com.dsg.wardstudy.common.exception.WSApiException;
import com.dsg.wardstudy.domain.comment.Comment;
import com.dsg.wardstudy.domain.comment.dto.CommentDto;
import com.dsg.wardstudy.domain.comment.repository.CommentRepository;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final StudyGroupRepository studyGroupRepository;

    @Override
    public CommentDto createComment(Long studyGroupId, CommentDto commentDto) {

        StudyGroup findStudyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NO_FOUND_ENTITY));

        Comment ofComment = Comment.of(commentDto);
        ofComment.setStudyGroup(findStudyGroup);
        Comment savedComment = commentRepository.save(ofComment);

        return CommentDto.mapToDto(savedComment);
    }

    @Override
    public List<CommentDto> getCommentsByStudyGroupId(Long studyGroupId) {

        List<Comment> comments = commentRepository.findByStudyGroupId(studyGroupId);

        return comments.stream()
                .map(CommentDto::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long studyGroupId, Long commentId) {

        StudyGroup findStudyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NO_FOUND_ENTITY, "no studyGroup"));

        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NO_FOUND_ENTITY, "no comment"));

        return CommentDto.mapToDto(findComment);
    }
}
