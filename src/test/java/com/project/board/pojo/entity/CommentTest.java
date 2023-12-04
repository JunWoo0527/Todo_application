package com.project.board.pojo.entity;

import com.project.board.comment.Comment;
import com.project.board.comment.CommentRepository;
import com.project.board.comment.CommentRequestDto;
import com.project.board.comment.CommentService;
import com.project.board.post.Post;
import com.project.board.post.PostRepository;
import com.project.board.post.PostRequestDto;
import com.project.board.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    @DisplayName("updateComment 메서드 테스트")
    void updateCommentTest() {
        // Given
        String content = "내용 테스트";
        String afterContent = "수정 내용 테스트";

        CommentRequestDto commentRequestDto = new CommentRequestDto(content);
        CommentRequestDto afterCommentRequestDto = new CommentRequestDto(afterContent);

        Comment testComment = new Comment(commentRequestDto);

        // When
        testComment.updateComment(afterCommentRequestDto);

        // then
        assertEquals(afterContent, testComment.getContent());
    }
}