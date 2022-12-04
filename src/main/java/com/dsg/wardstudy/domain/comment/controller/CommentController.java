package com.dsg.wardstudy.domain.comment.controller;

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

    @PostMapping("/study-group/{studyGroupId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long studyGroupId,
            @Valid @RequestBody CommentDto commentDto) {

        log.info("createComment run, studyGroupId: {}, commentDto: {}", studyGroupId, commentDto);
        return new ResponseEntity<>(commentService.createComment(studyGroupId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/study-group/{studyGroupId}/comments")
    public List<CommentDto> getCommentsByStudyGroupId(
            @PathVariable Long studyGroupId
    ) {
        log.info("getCommentsByStudyGroupId run, studyGroupId: {}", studyGroupId);
        return commentService.getCommentsByStudyGroupId(studyGroupId);
    }

    @GetMapping("/study-group/{studyGroupId}/comments/{commentId}")
    public CommentDto getCommentById(
            @PathVariable Long studyGroupId,
            @PathVariable Long commentId
    ) {
        log.info("getCommentById run, studyGroupId: {}, commentId: {}", studyGroupId, commentId);
        return commentService.getCommentById(studyGroupId, commentId);
    }

    @PutMapping("/study-group/{studyGroupId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long studyGroupId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDto commentDto
    ) {
        log.info("updateComment run, studyGroupId: {}, commentId: {}, commentDto: {}",
                studyGroupId, commentId, commentDto);
        return ResponseEntity.ok(commentService.updateComment(studyGroupId, commentId, commentDto));
    }

    @DeleteMapping("/study-group/{studyGroupId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long studyGroupId,
            @PathVariable Long commentId
    ) {
        log.info("deleteComment run, studyGroupId: {}, commentId: {}", studyGroupId, commentId);
        commentService.deleteComment(studyGroupId, commentId);
        return new ResponseEntity<>("comment deleteAll successfully.", HttpStatus.OK);
    }

}
