package com.project.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.board.dto.LoginRequestDto;
import com.project.board.dto.PostRequestDto;
import com.project.board.dto.PostResponseDto;
import com.project.board.jwt.JwtUtil;
import com.project.board.service.PostService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    public PostController(PostService postService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    // 할일카드 전체 출력
    @GetMapping("/post")
    public List<PostResponseDto> printPostControl() {
        return postService.printAllPost();
    }

    // 선택 할일카드 출력
    @GetMapping("/post/{postId}")
    public PostResponseDto printPostControl(@PathVariable("postId") Long postId) {
        return postService.printPost(postId);
    }

    // 할일카드 생성
    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> createPostControl(@RequestBody PostRequestDto requestDto, HttpServletRequest req){

        PostResponseDto postResponseDto = postService.createPost(requestDto, req);

        return ResponseEntity.status(201)
                .body(postResponseDto);
    }

    // 할일카드 수정
    @PutMapping("/post/{postId}")
    public PostResponseDto updatePostControl(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto) {
        return postService.updatePost(postId, postRequestDto);
    }

    // 게시글 삭제
    @DeleteMapping("/post/{postId}") //Request Parm방식으로 데이터를받음
    public Long deletePostControl(@PathVariable Long postId) {
        return postService.deletePost(postId);
    }






}
