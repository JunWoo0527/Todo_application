package com.project.board.controller;

import com.project.board.dto.PostRequestDto;
import com.project.board.dto.PostResponseDto;
import com.project.board.service.PostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {

    private PostService postService = new PostService();


    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        return postService.postPost(requestDto);
    }





}
