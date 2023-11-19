package com.project.board.service;

import com.project.board.dto.PostRequestDto;
import com.project.board.dto.PostResponseDto;
import com.project.board.entity.Post;
import com.project.board.entity.User;
import com.project.board.jwt.JwtUtil;
import com.project.board.repository.PostRepository;
import com.project.board.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    Map<String,List<PostResponseDto>> userPostList = new HashMap<>();


    public PostService(PostRepository postRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // 할일카드 저장
    public PostResponseDto createPost(PostRequestDto postRequestDto, HttpServletRequest req ) {
        // jwt토큰에서 username추출
        String username = jwtUtil.getUsernameFromToken(req);

        // RequestDto -> Entity
        Post post = new Post(postRequestDto);

        // username으로 User찾아 Post에 set
        User user = userRepository.findByUsername(username).orElseThrow();
        post.setUser(user);

        // DB에 저장
        Post savePost = postRepository.save(post);

        // Entity -> ResponseDto
        PostResponseDto postResponseDto = new PostResponseDto(savePost);

        return postResponseDto;
    }

    // 선택 할일카드 출력
    public PostResponseDto printPost(Long id) {

        Optional<Post> checkPostId = postRepository.findById(id);

        if (checkPostId.isPresent()){
            return new PostResponseDto(checkPostId.get());
        } else {
            return null;
        }

    }

    // 할일카드 전체 출력
    public Map<String , List<PostResponseDto>> printAllPost() {

        // Map구조로 key는 User, value는 해당 User가 작성한 Post들을 List구조로 저장
        for (User user : userRepository.findAll()){
            userPostList.put(user.getUsername(), postRepository.findAllByUserOrderByModifiedAtDesc(user).stream().map(PostResponseDto::new).toList());
        }

        return userPostList;
    }

    // 할일카드 수정
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, HttpServletRequest req) {
        //  로그인한 사용자와 할일카드작성자가 같은지 대조
        Post post = checkLoginUserAndPostUser(id, req);
        if (post == null){
            return null;
        }

        // Post 내용 수정
        post.update(postRequestDto);
        postRepository.save(post);

        // Entity -> Dto
        PostResponseDto postResponseDto = new PostResponseDto(post);

        return postResponseDto;
    }

    // 할일카드 완료처리
    public Boolean completePost(Long id, HttpServletRequest req) {
        //  로그인한 사용자와 할일카드작성자가 같은지 대조
        Post post = checkLoginUserAndPostUser(id, req);
        if (post == null){
            return false;
        }

        // 할일카드 완료처리
        post.setComplete(true);
        postRepository.save(post);

        return true;
    }

    // 로그인한 사용자와 게시글작성자 대조
    public Post checkLoginUserAndPostUser(Long id, HttpServletRequest req){
        // ID로 할일카드 DB조회
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()){
            return null;
        }
        Post post = optionalPost.get();

        // 로그인한 사용자의 username 추출 및 해당 사용자가 작성한 할일카드 조회
        String username = jwtUtil.getUsernameFromToken(req);
        List<Post> postByUsername = postRepository.findALLByUser_Username(username);
        if (postByUsername.isEmpty()){
            return null;
        }

        // 게시글 작성자와 로그인한 작성자가 일치하는지 검증
        String idUsername = post.getUser().getUsername();
        if (!Objects.equals(username, idUsername)){
            return null;
        }
        return post;
    }

}
