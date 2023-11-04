package com.project.board.controller;

import com.project.board.dto.PostRequestDto;
import com.project.board.dto.PostResponseDto;
import com.project.board.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 전체 출력
    @GetMapping("/post")
    public List<PostResponseDto> printPostControl() {
        return postService.printAllPost();
    }

    // 선택 게시글 출력
    @GetMapping("/post/{id}")
    public PostResponseDto printPostControl(@PathVariable("id") Long id) {
        return postService.printPost(id);
    }

    // 게시글 생성
    @PostMapping("/post")
    public PostResponseDto createPostControl(@RequestBody PostRequestDto requestDto) {
        return postService.createPost(requestDto);
    }

    // 게시글 수정
    @PutMapping("/post/{id}")
    public PostResponseDto updatePostControl(@PathVariable("id") Long id, @RequestBody(required = false) PostRequestDto postRequestDto) {
        return postService.updatePost(id, postRequestDto);
    }

    // 게시글 삭제
    @DeleteMapping("/post/{id}") //Delete 메서드는 Body를 가지지못함으로 Request Parm방식으로 데이터를받음
    public String deletePostControl(@PathVariable("id") Long id, @RequestParam String password) {
        return  postService.deletePost(id, password);
    }






}
