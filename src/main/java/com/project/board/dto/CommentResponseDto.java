package com.project.board.dto;

import com.project.board.entity.Comment;
import lombok.Getter;

@Getter
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
