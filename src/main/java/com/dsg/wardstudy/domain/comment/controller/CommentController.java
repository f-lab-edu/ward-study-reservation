package com.dsg.wardstudy.domain.comment.controller;

import com.dsg.wardstudy.common.auth.AuthUser;
import com.dsg.wardstudy.domain.comment.dto.CommentDto;
import com.dsg.wardstudy.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/study-group/{studyGroupId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long studyGroupId,
            @Valid @RequestBody CommentDto commentDto,
            @AuthUser Long userId
        ) {
        log.info("createComment run, studyGroupId: {}, commentDto: {}", studyGroupId, commentDto);
        log.info("createComment userId: {}", userId);
        return new ResponseEntity<>(commentService.createComment(studyGroupId, userId, commentDto), HttpStatus.CREATED);
    }

    // 특정스터디그룹 댓글 리스트 가져오기
    @GetMapping("/study-group/{studyGroupId}/comments")
    public List<CommentDto> getCommentsByStudyGroupId(
            @PathVariable Long studyGroupId
    ) {
        log.info("getCommentsByStudyGroupId run, studyGroupId: {}", studyGroupId);
        return commentService.getCommentsByStudyGroupId(studyGroupId);
    }

    // 특정 댓글 1개 상세보기
    @GetMapping("/study-group/{studyGroupId}/comments/{commentId}")
    public CommentDto getCommentById(
            @PathVariable Long studyGroupId,
            @PathVariable Long commentId,
            @AuthUser Long userId
    ) {
        log.info("getCommentById run, studyGroupId: {}, commentId: {}", studyGroupId, commentId);
        log.info("getCommentById userId: {}", userId);
        return commentService.getCommentById(studyGroupId, userId, commentId);
    }

    // 댓글 수정
    @PutMapping("/study-group/{studyGroupId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long studyGroupId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDto commentDto,
            @AuthUser Long userId
    ) {
        log.info("updateComment run, studyGroupId: {}, commentId: {}, commentDto: {}",
                studyGroupId, commentId, commentDto);
        log.info("updateComment userId: {}", userId);
        return ResponseEntity.ok(commentService.updateComment(studyGroupId, userId, commentId, commentDto));
    }

    // 댓글 삭제
    @DeleteMapping("/study-group/{studyGroupId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long studyGroupId,
            @PathVariable Long commentId,
            @AuthUser Long userId
    ) {
        log.info("deleteComment run, studyGroupId: {}, commentId: {}", studyGroupId, commentId);
        log.info("deleteComment userId: {}", userId);
        commentService.deleteComment(studyGroupId, userId, commentId);
        return new ResponseEntity<>("comment deleteAll successfully.", HttpStatus.OK);
    }

}
