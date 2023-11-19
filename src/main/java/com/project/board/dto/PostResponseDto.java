package com.project.board.dto;

import com.project.board.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String username;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private Boolean complete;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUser().getUsername();
        this.content = post.getContent();
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.complete = post.getComplete();
    }





}
