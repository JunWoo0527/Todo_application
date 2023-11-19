package com.project.board.controller;

import com.project.board.dto.CommentRequestDto;
import com.project.board.dto.CommentResponseDto;
import com.project.board.dto.PostResponseDto;
import com.project.board.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
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
    public ResponseEntity createCommentControl(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest req) {
        CommentResponseDto commentResponseDto = commentService.createComment(postId, commentRequestDto, req);
        if (commentResponseDto == null){
            return ResponseEntity.status(400).body("해당 할일카드가 존재하지 않습니다.");
        }
        return ResponseEntity.ok().body(commentResponseDto);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity updateCommentControl(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest req) {
        CommentResponseDto commentResponseDto = commentService.updateComment(commentId, commentRequestDto, req);

        if (commentResponseDto == null){
            return ResponseEntity.status(400).body("작성자만 수정할수있습니다.");
        }
        return ResponseEntity.ok().body(commentResponseDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteCommentControl(@PathVariable Long commentId, HttpServletRequest req) {
        boolean check = commentService.deleteComment(commentId, req);
        if (check == false){
            return ResponseEntity.status(400).body("작성자만 삭제할 수 있습니다.");
        }

        return  ResponseEntity.ok().body("삭제에 성공하였습니다.");
    }

}
