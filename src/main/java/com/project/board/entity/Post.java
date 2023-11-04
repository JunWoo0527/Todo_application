package com.project.board.entity;

import com.project.board.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String writer;
    private String password;
    private String content;
    private String date;

    public Post(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.writer = requestDto.getWriter();
        this.password = requestDto.getPassword();
        this.content = requestDto.getContent();
        this.date = requestDto.getDate();
    }

    public void update(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.writer = post.getWriter();
        this.content = post.getContent();
        this.date = post.getDate();
    }
}
