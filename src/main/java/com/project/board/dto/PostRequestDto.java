package com.project.board.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title;
    private String writer;
    private String password;
    private String content;
    private String date;
}
