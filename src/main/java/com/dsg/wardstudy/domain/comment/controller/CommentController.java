package com.dsg.wardstudy.domain.comment.controller;

import com.dsg.wardstudy.domain.comment.dto.CommentDto;
import com.dsg.wardstudy.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/study-group/{studyGroupId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long studyGroupId,
            @Valid @RequestBody CommentDto commentDto) {

        return new ResponseEntity<>(commentService.createComment(studyGroupId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/study-group/{studyGroupId}/comments")
    public List<CommentDto> getCommentsByStudyGroupId(@PathVariable Long studyGroupId) {
        return commentService.getCommentsByStudyGroupId(studyGroupId);
    }

    @GetMapping("/study-group/{studyGroupId}/comments/{commentId}")
    public CommentDto getCommentById(
            @PathVariable Long studyGroupId,
            @PathVariable Long commentId
    ) {
        return commentService.getCommentById(studyGroupId, commentId);
    }

}
