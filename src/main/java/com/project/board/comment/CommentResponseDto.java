package com.project.board.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long Id;
    private Long postId;
    private String commentUsername;
    private String commentContent;

    public CommentResponseDto(Comment comment){
        this.Id = comment.getId();
        this.postId = comment.getPost().getId();
        this.commentUsername = comment.getUser().getUsername();
        this.commentContent = comment.getContent();
    }
}
