package com.project.board.service;

import com.project.board.dto.PostRequestDto;
import com.project.board.dto.PostResponseDto;
import com.project.board.entity.Post;
import com.project.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto postPost(PostRequestDto requestDto) {

        // RequestDto -> Entity
        Post post = new Post(requestDto);

        // Entity ID 부여 및 postList에 저장
        postRepository.savePost(post);

        // Entity -> ResponseDto
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }









}
