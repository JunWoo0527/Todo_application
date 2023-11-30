package com.project.board.post;

import com.project.board.jwt.JwtUtil;
import com.project.board.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Map<String, List<PostResponseDto>>> printPostControl() {
        Map<String, List<PostResponseDto>> userPostList =  postService.printAllPost();

//        if (userPostList.isEmpty()){
//            return ResponseEntity.status(400).body("현재 게시글이 존재하지 않습니다.");
//        }
        return ResponseEntity.ok().body(userPostList);
    }

    // 선택 할일카드 출력
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> printPostControl(@PathVariable("postId") Long postId) {

        PostResponseDto postResponseDto = postService.printPost(postId);

//        if (postResponseDto == null){
//            return ResponseEntity.status(400).body("존재하지 않는 ID입니다.");
//        }
        return ResponseEntity.ok().body(postResponseDto);
    }

    // 할일카드 생성
    @PostMapping("")
    public ResponseEntity createPostControl(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(201)
                .body(postService.createPost(requestDto, userDetails));
    }

    // 할일카드 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePostControl(@PathVariable Long postId,
                                            @RequestBody PostRequestDto postRequestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostResponseDto postResponseDto = postService.updatePost(postId, postRequestDto, userDetails);

//        if (postResponseDto == null){
//            return ResponseEntity.status(400).body("잘못된 입력입니다. 확인후 다시 입력해주세요");
//        }
        return ResponseEntity.accepted().body(postResponseDto);
    }

    // 할일카드 완료
    @PatchMapping("/check/{postId}") //Request Parm방식으로 데이터를받음
    public ResponseEntity<String> completePostControl(@PathVariable Long postId,  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Boolean check = postService.completePost(postId,userDetails);
//        if (check == false){
//            return ResponseEntity.status(400).body("작성자만 완료처리할 수 있습니다.");
//        }
        return ResponseEntity.ok("완료처리 되었습니다.");
    }

}
