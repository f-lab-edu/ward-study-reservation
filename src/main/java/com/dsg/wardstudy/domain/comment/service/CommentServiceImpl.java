package com.dsg.wardstudy.domain.comment.service;

import com.dsg.wardstudy.common.exception.ErrorCode;
import com.dsg.wardstudy.common.exception.WSApiException;
import com.dsg.wardstudy.domain.comment.entity.Comment;
import com.dsg.wardstudy.domain.comment.dto.CommentDto;
import com.dsg.wardstudy.domain.comment.repository.CommentRepository;
import com.dsg.wardstudy.domain.studyGroup.entity.StudyGroup;
import com.dsg.wardstudy.domain.user.entity.User;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final StudyGroupRepository studyGroupRepository;

    private final UserRepository userRepository;

    @Transactional
    @Override
    public CommentDto createComment(Long studyGroupId, Long userId, CommentDto commentDto) {

        StudyGroup findStudyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NO_FOUND_ENTITY, "no studyGroup"));
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NO_FOUND_ENTITY, "no user"));

        commentDto.crateUserInfo(findUser);
        Comment ofComment = Comment.of(commentDto);
        ofComment.setStudyGroup(findStudyGroup);
        ofComment.setUser(findUser);
        Comment savedComment = commentRepository.save(ofComment);

        return CommentDto.mapToDto(savedComment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getCommentsByStudyGroupId(Long studyGroupId) {

        List<Comment> comments = commentRepository.findByStudyGroupId(studyGroupId);
        return comments.stream()
                .map(CommentDto::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CommentDto getCommentById(Long studyGroupId, Long userId, Long commentId) {

        Comment findComment = validateComment(studyGroupId, userId, commentId);
        return CommentDto.mapToDto(findComment);
    }



    @Transactional
    @Override
    public CommentDto updateComment(Long studyGroupId, Long userId, Long commentId, CommentDto commentDto) {
        Comment findComment = validateComment(studyGroupId, userId, commentId);

        findComment.updateComment(commentDto);
        return CommentDto.mapToDto(findComment);
    }

    @Transactional
    @Override
    public void deleteComment(Long studyGroupId, Long userId, Long commentId) {
        Comment findComment = validateComment(studyGroupId, userId, commentId);

        commentRepository.deleteById(findComment.getId());
    }

    private Comment validateComment(Long studyGroupId, Long userId, Long commentId) {
        StudyGroup findStudyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NO_FOUND_ENTITY, "no studyGroup"));

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NO_FOUND_ENTITY, "no user"));

        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NO_FOUND_ENTITY, "no comment"));

        return findComment;
    }
}
