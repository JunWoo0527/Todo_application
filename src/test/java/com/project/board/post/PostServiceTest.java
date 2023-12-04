package com.project.board.post;

import com.project.board.post.*;
import com.project.board.security.UserDetailsImpl;
import com.project.board.user.User;
import com.project.board.user.UserRepository;
import com.project.board.user.UserRoleEnum;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private static Post testPost;
    private static User testUser;

    @Mock
    static PostRepository postRepository;
    @Mock
    static UserRepository userRepository;
    @InjectMocks
    static PostService postService;

    @BeforeAll
    static void mockUserPostSetup() {
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
        postService = new PostService(postRepository, userRepository);
    }

    @Test
    @DisplayName("할일카드 작성 성공")
    void createPost() {
        // Given
        PostRequestDto testPosteRequestDto = new PostRequestDto("createTitle", "createContent");
        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);

        Post post = new Post(testPosteRequestDto);
        post.setUser(testUser);

        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(testUser));
        given(postRepository.save(any(Post.class))).willReturn(post);

        // When
        PostResponseDto testpostResponseDto = postService.createPost(testPosteRequestDto, userDetails);

        // Then
        assertEquals("createTitle", testpostResponseDto.getTitle());
        assertEquals("createContent", testpostResponseDto.getContent());
        assertEquals("junwoo", testpostResponseDto.getUsername());
    }

    @Test
    @DisplayName("선택 할일카드 출력 성공")
    void printPost() {
        // Given
        Long postId = 10L;
        testPost.setId(postId);
        testPost.setUser(new User());

        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(testPost));

        // When
        PostResponseDto postResponseDto = postService.printPost(postId);

        // Then
        assertEquals(10L, postResponseDto.getId());
        assertEquals("title", postResponseDto.getTitle());
        assertEquals("content", postResponseDto.getContent());
    }

    @Test
    @DisplayName("전체 할일카드 출력 성공")
    void printAllPost() {
        // Given
        testPost.setUser(testUser);
        Post testpost2 = new Post();
        testpost2.setUser(testUser);

        User testUser2 = new User();
        testUser2.setUsername("2");
        Post testpost3 = new Post();
        testpost3.setUser(testUser2);

        List<User> testUserList = new ArrayList<>();
        testUserList.add(testUser);
        testUserList.add(testUser2);

        Map<String, List<PostResponseDto>> testUserPostList = new HashMap<>();
        testUserPostList.put("junwoo", Arrays.asList(testPost, testpost2).stream().map(PostResponseDto::new).toList());
        testUserPostList.put("2", Arrays.asList(testpost3).stream().map(PostResponseDto::new).toList());

        given(userRepository.findAll()).willReturn(testUserList);

        given(postRepository.findAllByUserOrderByModifiedAtDesc(testUser)
                ).willReturn(List.of(testPost, testpost2));
        given(postRepository.findAllByUserOrderByModifiedAtDesc(testUser2)
                ).willReturn(List.of(testpost3));

        // When
        Map<String, List<PostResponseDto>> userPostList = postService.printAllPost();

        // Then
        assertEquals(2, userPostList.size());
        assertEquals(testUserPostList.get("junwoo").getClass(), userPostList.get("junwoo").getClass());
        assertEquals(testUserPostList.get("2").getClass(), userPostList.get("2").getClass());
    }

    @Test
    @DisplayName("할일카드 수정 성공")
    void updatePost() {
        // Given
        Long postId = 10L;
        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);
        PostRequestDto postRequestDto = new PostRequestDto("수정 제목", "수정 내용");

        testPost.setUser(testUser);
        List<Post> testPostList = new ArrayList<>();
        testPostList.add(testPost);

        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(testPost));
        given(postRepository.findALLByUser_Username(any(String.class))).willReturn(testPostList);
        given(postRepository.save(any(Post.class))).willReturn(testPost);

        // When
        PostResponseDto postResponseDto = postService.updatePost(postId, postRequestDto, userDetails);

        // Then
        assertNotEquals("title", postResponseDto.getTitle());
        assertNotEquals("content", postResponseDto.getContent());
        assertEquals("수정 제목", postResponseDto.getTitle());
        assertEquals("수정 내용", postResponseDto.getContent());

    }

    @Test
    @DisplayName("할일카드 완료 성공")
    void completePost() {
        // Given
        Long postId = 10L;
        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);

        testPost.setUser(testUser);
        List<Post> testPostList = new ArrayList<>();
        testPostList.add(testPost);

        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(testPost));
        given(postRepository.findALLByUser_Username(any(String.class))).willReturn(testPostList);
        given(postRepository.save(any(Post.class))).willReturn(testPost);

        // When
        Boolean completeTest = postService.completePost(postId, userDetails);

        // Then
        assertTrue(completeTest);
    }

    @Test
    @DisplayName("로그인한 사용자와 게시글 작성자 비교 성공")
    void checkLoginUserAndPostUser() {
        // Given
        Long postId = 10L;
        UserDetailsImpl userDetails = new UserDetailsImpl(testUser);
        testPost.setUser(testUser);
        testPost.setId(postId);

        List<Post> testPostList = new ArrayList<>();
        testPostList.add(testPost);

        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(testPost));
        given(postRepository.findALLByUser_Username(any(String.class))).willReturn(testPostList);

        // When
        Post post = postService.checkLoginUserAndPostUser(postId, userDetails);

        // Then
        assertEquals(testPost.getUser(), post.getUser());
        assertEquals(testPost.getId(), post.getId());
        assertEquals(testPost.getTitle(), post.getTitle());
        assertEquals(testPost.getContent(), post.getContent());
    }

}