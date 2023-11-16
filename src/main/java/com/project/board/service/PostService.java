package com.project.board.service;

import com.project.board.dto.PostRequestDto;
import com.project.board.dto.PostResponseDto;
import com.project.board.entity.Post;
import com.project.board.jwt.JwtAuthorizationFilter;
import com.project.board.jwt.JwtUtil;
import com.project.board.repository.PostRepository;
import com.project.board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;


    public PostService(PostRepository postRepository, UserRepository userRepository, JwtUtil jwtUtil, JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    // 게시글 저장
    public PostResponseDto createPost(PostRequestDto postRequestDto ) {


        // RequestDto -> Entity
        Post post = new Post(postRequestDto);
        post.getCreatedAt();


        // DB에 저장
        Post savePost = postRepository.save(post);

        // Entity -> ResponseDto
        PostResponseDto postResponseDto = new PostResponseDto(savePost);

        return postResponseDto;
    }

    // 선택 게시글 출력
    public PostResponseDto printPost(Long id) {
        PostResponseDto postResponseDto = new PostResponseDto(findPost(id));
        return postResponseDto;
    }

    // 게시글 전체 출력
    public List<PostResponseDto> printAllPost() {

        return postRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    // 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Post post = findPost(id);

        // Post 내용 수정
        post.update(postRequestDto);

        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }


    // 게시글 삭제
    public Long deletePost(Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Post post = findPost(id);

        // memo 삭제
        postRepository.delete(post);

        return id;
    }

    private Post findPost(Long id) {
       return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다."));
    }
}
