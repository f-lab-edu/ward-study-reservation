package com.dsg.wardstudy.domain.comment.dto;

import com.dsg.wardstudy.domain.comment.Comment;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDto {

    private Long id;

    private String name;

    private String email;

    private String body;

    @Builder
    public CommentDto(Long id, String name, String email, String body) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.body = body;
    }

    public static CommentDto mapToDto(Comment savedComment) {
        return CommentDto.builder()
                .id(savedComment.getId())
                .name(savedComment.getName())
                .email(savedComment.getEmail())
                .body(savedComment.getBody())
                .build();
    }
}
