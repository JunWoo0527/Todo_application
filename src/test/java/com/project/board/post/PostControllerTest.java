package com.project.board.post;

import com.project.board.security.UserDetailsImpl;
import com.project.board.test.ControllerTest;
import com.project.board.test.PostCommonTest;
import com.project.board.test.PostTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest extends ControllerTest implements PostCommonTest {

    @MockBean
    private PostService postService;

    @Test
    @DisplayName("할일카드 생성 요청")
    void createPostControl() throws Exception{
        // When
        var action = mockMvc.perform(post("/api/post")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_POST_REQUEST_DTO)));

        // Then
        action.andExpect(status().isCreated());
        verify(postService, times(1)).createPost(any(PostRequestDto.class), eq(testUserDetails));
    }

    @Test
    @DisplayName("전체 할일카드 출력 요청")
    void printAllPostControl() throws Exception {
        // Given
        var testPost1 = PostTestUtils.get(TEST_POST, 1L, LocalDateTime.now(), TEST_USER);
        var testPost2 = PostTestUtils.get(TEST_POST, 2L, LocalDateTime.now().minusMinutes(1), TEST_USER);
        var testAnotherPost = PostTestUtils.get(TEST_POST, 3L, LocalDateTime.now(), TEST_ANOTHER_USER);

        given(postService.printAllPost()).willReturn(
                Map.of(TEST_USER_NAME, List.of(new PostResponseDto(testPost1), new PostResponseDto(testPost2)),
                        ANOTHER_PREFIX + TEST_USER_NAME, List.of(new PostResponseDto(testAnotherPost))));


        // When
        var action = mockMvc.perform(get("/api/post")
                        .accept(MediaType.APPLICATION_JSON));

        // Then
        action.andExpect(status().isOk());
//                .andExpect(jsonPath("$[?(@.username == '" + TEST_USER_NAME + "')].todoList[*].id")
//                        .value(Matchers.containsInAnyOrder(testPost1.getId().intValue(), testPost2.getId().intValue())))
//                .andExpect(jsonPath("$[?(@ =='" + ANOTHER_PREFIX + TEST_USER_NAME + "')].todoList[*].id")
//                        .value(Matchers.containsInAnyOrder(testAnotherPost.getId().intValue())));
        verify(postService, times(1)).printAllPost();


    }
    @Nested
    @DisplayName("선택 할일카드 출력 요청")
    class printPost {
        @Test
        @DisplayName("선택 할일카드 출력 요청 성공")
        void PrintPostControl_success() throws Exception {
            // Given
            TEST_POST.setUser(TEST_USER);
            PostResponseDto tesetPostResponseDto = new PostResponseDto(TEST_POST);
            given(postService.printPost(eq(TEST_POST_ID))).willReturn(tesetPostResponseDto);

            // When
            var action = mockMvc.perform(get("/api/post/{postId}", TEST_POST_ID)
                    .accept(MediaType.APPLICATION_JSON));

            // Then
            action.andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(TEST_POST_TITLE))
                    .andExpect(jsonPath("$.content").value(TEST_POST_CONTENT));
        }

        @Test
        @DisplayName("선택 할일카드 출력 요청 실패 : 존재하지않는 할일카드 ID")
        void PrintPostControl_fail() throws  Exception {
            // Given
            TEST_POST.setUser(TEST_USER);
            given(postService.printPost(eq(TEST_POST_ID))).willThrow(new IllegalArgumentException());

            // When
            var action = mockMvc.perform(get("/api/post/{postId}", TEST_POST_ID)
                    .accept(MediaType.APPLICATION_JSON));

            // Then
            action.andExpect(status().isBadRequest());


        }
    }
    @Nested
    @DisplayName("할일카드 수정 요청")
    class updatePost {
        @Test
        @DisplayName("할일카드 수정 요청 성공")
        void updatePostControl_success() throws Exception {
            // Given
            TEST_POST.setUser(TEST_USER);
            PostResponseDto tesetPostResponseDto = new PostResponseDto(TEST_POST);
            given(postService.updatePost(eq(TEST_POST_ID), eq(TEST_POST_REQUEST_DTO), any(UserDetailsImpl.class)))
                    .willReturn(tesetPostResponseDto);

            // When
            var action = mockMvc.perform(put("/api/post/{postId}", TEST_POST_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(TEST_POST_REQUEST_DTO)));

            // Then
            action.andExpect(status().isAccepted());
//                    .andExpect(jsonPath("$.title").value(TEST_POST_TITLE))
//                    .andExpect(jsonPath("$.content").value(TEST_POST_CONTENT));
        }

//        @Test
//        @DisplayName("할일카드 수정 요청 실패")
//        void updatePostControl_fail() throws Exception {
//            // Given
//            given(postService.updatePost(eq(TEST_POST_ID), eq(TEST_POST_REQUEST_DTO), any(UserDetailsImpl.class)))
//                    .willThrow(new IllegalArgumentException());
//
//            // When
//            var action = mockMvc.perform(put("/api/post/{postId}", TEST_POST_ID)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(TEST_POST_REQUEST_DTO)));
//
//            // Then
//            action.andExpect(status().isBadRequest());
//
//        }

    }

//    @Test
//    @DisplayName("할일 완료 요청 성공")
//    void completePostControl() {
//        // Given
//        TEST_POST.setComplete(true);
//        Post
//        // When
//
//        // Then
//    }
}