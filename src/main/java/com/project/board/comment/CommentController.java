package com.project.board.comment;

import com.project.board.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 작성
    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDto> createCommentControl(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto commentResponseDto = commentService.createComment(postId, commentRequestDto, userDetails);
//        if (commentResponseDto == null){
//            return ResponseEntity.status(400).body("해당 할일카드가 존재하지 않습니다.");
//        }
        return ResponseEntity.ok().body(commentResponseDto);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateCommentControl(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto commentResponseDto = commentService.updateComment(commentId, commentRequestDto, userDetails);

//        if (commentResponseDto == null){
//            return ResponseEntity.status(400).body("작성자만 수정할수있습니다.");
//        }
        return ResponseEntity.ok().body(commentResponseDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteCommentControl(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean check = commentService.deleteComment(commentId, userDetails);
//        if (check == false){
//            return ResponseEntity.status(400).body("작성자만 삭제할 수 있습니다.");
//        }

        return  ResponseEntity.ok().body("삭제에 성공하였습니다.");
    }

}
