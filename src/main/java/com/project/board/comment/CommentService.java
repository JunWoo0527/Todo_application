package com.project.board.comment;

import com.project.board.post.Post;
import com.project.board.jwt.JwtUtil;
import com.project.board.post.PostRepository;
import com.project.board.user.UserRepository;
import com.project.board.security.UserDetailsImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        // 할일카드 DB조회
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 할일카드가 존재하지 않습니다.")
                );


        // jwt토큰에서 username추출
        String username = userDetails.getUsername();

        // 댓글 인스턴스화
        Comment comment = new Comment(commentRequestDto);
        comment.setPost(post);
        comment.setUser(userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalAccessError("해당 올바르지 못한 사용자입니다.")
                ));

        // 댓글 Repository 저장
        commentRepository.save(comment);

        // Entity -> Dto
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

        return commentResponseDto;
    }

    // 댓글 수정
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {

        //댓글 유무 및 로그인사용자 와 댓글작성자 대조 검증
        Comment comment = checkLoginUserAndCommentUser(commentId, userDetails);


        comment.updateComment(commentRequestDto);
        commentRepository.save(comment);

        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return commentResponseDto;
    }

    // 댓글 삭제
    public boolean deleteComment(Long commentId, UserDetailsImpl userDetails) {

        //댓글 유무 및 로그인사용자 와 댓글작성자 대조 검증
        Comment comment = checkLoginUserAndCommentUser(commentId, userDetails);


        commentRepository.delete(comment);

        return true;
    }

    // 로그인한 사용자와 댓글 작성자 대조
    public Comment checkLoginUserAndCommentUser(Long id, UserDetailsImpl userDetails){
        // 댓글 DB조회
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 ID의 댓글이 존재하지 않습니다.")
        );

        // 로그인한 사용자의 username 추출 및 해당 사용자가 작성한 댓글 조회
        String username = userDetails.getUsername();
        List<Comment> CommentByUsername =commentRepository.findALLByUser_Username(username);
        if (CommentByUsername.isEmpty()){
            throw new NullPointerException("로그인한 사용자가 작성한 댓글이 존재하지 않습니다.");
        }

        // 댓글 작성자와 로그인한 작성자가 일치하는지 검증
        String idUsername = comment.getUser().getUsername();
        if (!Objects.equals(username, idUsername)){
            throw new IllegalAccessError("해당 할일카드는 작성자만 수정할 수 있습니다.");
        }
        return comment;
    }


}
