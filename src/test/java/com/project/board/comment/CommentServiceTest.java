package com.project.board.comment;

import com.project.board.comment.*;
import com.project.board.post.Post;
import com.project.board.post.PostRepository;
import com.project.board.post.PostRequestDto;
import com.project.board.security.UserDetailsImpl;
import com.project.board.user.User;
import com.project.board.user.UserRepository;
import com.project.board.user.UserRoleEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    private static User testUser;
    private static Post testPost;

    @Mock
    static PostRepository postRepository;
    @Mock
    static UserRepository userRepository;
    @Mock
    static CommentRepository commentRepository;
    @InjectMocks
    static CommentService commentService;

    @BeforeAll
    static void testUserSetup() {
        // 테스트 유저 생성
        String username = "junwoo";
        String password = "abcd1234";
        UserRoleEnum role = UserRoleEnum.USER;
        testUser = new User(username, password, role);

        // 테스트 할일카드 생성
        String title = "title";
        String content = "content";
        PostRequestDto postRequestDto = new PostRequestDto(title, content);
        testPost = new Post(postRequestDto);

    }

    @BeforeEach
    void repositoryReset() {
        commentService = new CommentService(postRepository, userRepository, commentRepository);
    }

    @Test
    @DisplayName("댓글 생성 성공")
    void createCommentTest() {
        // Given
        Long postId = 10l;

        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);

        CommentRequestDto commentRequestDto = new CommentRequestDto("comment content");
        Comment testComment = new Comment(commentRequestDto);
        testComment.setId(1L);
        testComment.setPost(testPost);
        testComment.setUser(testUser);

        CommentResponseDto testcommentResponseDto = new CommentResponseDto(testComment);

        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(testPost));
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(testUser));
        given(commentRepository.save(any(Comment.class))).willReturn(testComment);

        // When
        CommentResponseDto commentResponseDto = commentService.createComment(postId, commentRequestDto, userDetails);

        // Then
        assertEquals(testcommentResponseDto.getCommentContent(), commentResponseDto.getCommentContent());
        assertEquals(testcommentResponseDto.getCommentUsername(), commentResponseDto.getCommentUsername());
        assertEquals(testcommentResponseDto.getPostId(), commentResponseDto.getPostId());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateComment() {
        // Gvien
        Long commentId = 10L;
        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);

        Comment testComment = new Comment(new CommentRequestDto("edit content"));
        testComment.setUser(testUser);
        testComment.setPost(testPost);

        CommentRequestDto commentRequestDto = new CommentRequestDto("edit content");

        CommentResponseDto tesCommentResponseDto = new CommentResponseDto(testComment);

        List<Comment> testCommentList = new ArrayList<>();
        testCommentList.add(testComment);

        given(commentRepository.findById(any(Long.class))).willReturn(Optional.of(testComment));
        given(commentRepository.findALLByUser_Username(any(String.class))).willReturn(testCommentList);

        // When
        CommentResponseDto commentResponseDto = commentService.updateComment(commentId, commentRequestDto, userDetails);

        // Then
        assertEquals(tesCommentResponseDto.getCommentUsername(), commentResponseDto.getCommentUsername());
        assertEquals(tesCommentResponseDto.getCommentContent(), commentResponseDto.getCommentContent());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment() {
        // Given
        Long commentId = 10L;
        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);

        Comment testComment = new Comment(new CommentRequestDto("content"));
        testComment.setUser(testUser);
        testComment.setPost(testPost);


        List<Comment> testCommentList = new ArrayList<>();
        testCommentList.add(testComment);

        given(commentRepository.findById(any(Long.class))).willReturn(Optional.of(testComment));
        given(commentRepository.findALLByUser_Username(any(String.class))).willReturn(testCommentList);
        doNothing().when(commentRepository).delete(any(Comment.class));

        // When
        Boolean deleteTest = commentService.deleteComment(commentId, userDetails);

        // Then
        assertTrue(deleteTest);
        verify(commentRepository, times(1)).delete(any(Comment.class));


    }

    @Test
    @DisplayName("로그인한 사용자와 댓글 작성자 대조 성공")
    void checkLoginUserAndCommentUser() {
        // Given
        Long commentId = 10L;
        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);

        Comment testComment = new Comment(new CommentRequestDto("content"));
        testComment.setUser(testUser);
        testComment.setPost(testPost);


        List<Comment> testCommentList = new ArrayList<>();
        testCommentList.add(testComment);

        given(commentRepository.findById(any(Long.class))).willReturn(Optional.of(testComment));
        given(commentRepository.findALLByUser_Username(any(String.class))).willReturn(testCommentList);

        // When
        Comment comment = commentService.checkLoginUserAndCommentUser(commentId, userDetails);

        // Then
        assertEquals(testComment.getUser(), comment.getUser());
        assertEquals(testComment.getId(), comment.getId());
        assertEquals(testComment.getContent(), comment.getContent());
    }
}