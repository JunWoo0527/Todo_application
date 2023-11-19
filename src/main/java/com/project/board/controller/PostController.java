package com.project.board.controller;

import com.project.board.dto.PostRequestDto;
import com.project.board.dto.PostResponseDto;
import com.project.board.entity.Post;
import com.project.board.jwt.JwtUtil;
import com.project.board.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    public PostController(PostService postService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    // 할일카드 전체 출력
    @GetMapping("")
    public ResponseEntity printPostControl() {
        Map<String, List<PostResponseDto>> userPostList =  postService.printAllPost();

        if (userPostList.isEmpty()){
            return ResponseEntity.status(400).body("현재 게시글이 존재하지 않습니다.");
        }
        return ResponseEntity.ok().body(userPostList);
    }

    // 선택 할일카드 출력
    @GetMapping("/{postId}")
    public ResponseEntity printPostControl(@PathVariable("postId") Long postId) {

        PostResponseDto postResponseDto = postService.printPost(postId);

        if (postResponseDto == null){
            return ResponseEntity.status(400).body("존재하지 않는 ID입니다.");
        }
        return ResponseEntity.ok().body(postResponseDto);
    }

    // 할일카드 생성
    @PostMapping("")
    public ResponseEntity createPostControl(@RequestBody PostRequestDto requestDto, HttpServletRequest req){
        return ResponseEntity.status(201)
                .body(postService.createPost(requestDto, req));
    }

    // 할일카드 수정
    @PatchMapping("/{postId}")
    public ResponseEntity updatePostControl(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto, HttpServletRequest req) throws IOException {

        PostResponseDto postResponseDto = postService.updatePost(postId, postRequestDto, req);

        if (postResponseDto == null){
            return ResponseEntity.status(400).body("잘못된 입력입니다. 확인후 다시 입력해주세요");
        }
        return ResponseEntity.accepted().body(postResponseDto);
    }

    // 할일카드 완료
    @PatchMapping("/check/{postId}") //Request Parm방식으로 데이터를받음
    public ResponseEntity completePostControl(@PathVariable Long postId, HttpServletRequest req) {
        Boolean check = postService.completePost(postId,req);
        if (check == false){
            return ResponseEntity.status(400).body("작성자만 완료처리할 수 있습니다.");
        }
        return ResponseEntity.ok("완료처리 되었습니다.");
    }

}
