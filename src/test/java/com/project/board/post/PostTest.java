package com.project.board.post;

import com.project.board.post.Post;
import com.project.board.post.PostRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostTest {

    @Test
    @DisplayName("Post 생성 테스트(PostRequestDto)")
    public void test1() {
        // Given
        String title = "제목 테스트";
        String content = "내용 테스트";

        // When
        PostRequestDto postRequestDto = new PostRequestDto(title, content);
        Post testPost = new Post(postRequestDto);

        // then
        assertEquals(title, testPost.getTitle());
        assertEquals(content, testPost.getContent());
    }

    @Test
    @DisplayName("Post update 메서드 테스트")
    void update() {
        // Given
        String title = "제목 테스트";
        String content = "내용 테스트";
        String afterTitle = "수정 제목 테스트";
        String afterContent = "수정 내용 테스트";

        PostRequestDto postRequestDto = new PostRequestDto(title, content);
        PostRequestDto afterPostRequestDto = new PostRequestDto(afterTitle, afterContent);

        Post testPost = new Post(postRequestDto);

        // When
        testPost.update(afterPostRequestDto);

        // Then
        assertEquals(afterTitle, testPost.getTitle());
        assertEquals(afterContent, testPost.getContent());
    }
}