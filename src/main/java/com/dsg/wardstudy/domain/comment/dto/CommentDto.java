package com.dsg.wardstudy.domain.comment.dto;

import com.dsg.wardstudy.domain.comment.entity.Comment;
import com.dsg.wardstudy.domain.user.entity.User;
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
                .name(savedComment.getUser().getName())
                .email(savedComment.getUser().getEmail())
                .body(savedComment.getBody())
                .build();
    }

    public void crateUserInfo(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
