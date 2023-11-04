package com.project.board.dto;

import com.project.board.entity.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private String date;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.writer = post.getWriter();
        this.content = post.getContent();
        this.date = post.getDate();
    }




}
