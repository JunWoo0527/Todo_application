package com.project.board.service;

import com.project.board.dto.CommentRequestDto;
import com.project.board.dto.CommentResponseDto;
import com.project.board.entity.Comment;
import com.project.board.entity.Post;
import com.project.board.jwt.JwtUtil;
import com.project.board.repository.CommentRepository;
import com.project.board.repository.PostRepository;
import com.project.board.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    public CommentService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, JwtUtil jwtUtil) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.jwtUtil = jwtUtil;
    }

    // 댓글 생성
    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, HttpServletRequest req) {
        // 할일카드 DB조회
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()){
            return null;
        }
        Post post = optionalPost.get();

        // jwt토큰에서 username추출
        String username = jwtUtil.getUsernameFromToken(req);

        // 댓글 인스턴스화
        Comment comment = new Comment(commentRequestDto);
        comment.setPost(post);
        comment.setUser(userRepository.findByUsername(username).orElseThrow());

        // 댓글 Repository 저장
        commentRepository.save(comment);

        // Entity -> Dto
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

        return commentResponseDto;
    }

    // 댓글 수정
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, HttpServletRequest req) {

        //댓글 유무 및 로그인사용자 와 댓글작성자 대조 검증
        Comment comment = checkLoginUserAndCommentUser(commentId, req);
        if (comment == null) {
            return null;
        }

        comment.updateComment(commentRequestDto);
        commentRepository.save(comment);

        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return commentResponseDto;
    }

    // 댓글 삭제
    public boolean deleteComment(Long commentId, HttpServletRequest req) {

        //댓글 유무 및 로그인사용자 와 댓글작성자 대조 검증
        Comment comment = checkLoginUserAndCommentUser(commentId, req);
        if (comment == null) {
            return false;
        }

        commentRepository.delete(comment);

        return true;
    }

    // 로그인한 사용자와 댓글 작성자 대조
    public Comment checkLoginUserAndCommentUser(Long id, HttpServletRequest req){
        // 댓글 DB조회
        Optional<Comment> optionalComment =commentRepository.findById(id);
        if (optionalComment.isEmpty()){
            return null;
        }
        Comment comment = optionalComment.get();

        // 로그인한 사용자의 username 추출 및 해당 사용자가 작성한 댓글 조회
        String username = jwtUtil.getUsernameFromToken(req);
        List<Comment> CommentByUsername =commentRepository.findALLByUser_Username(username);
        if (CommentByUsername.isEmpty()){
            return null;
        }

        // 댓글 작성자와 로그인한 작성자가 일치하는지 검증
        String idUsername = comment.getUser().getUsername();
        if (!Objects.equals(username, idUsername)){
            return null;
        }
        return comment;
    }


}
