package com.project.board.post;

import com.project.board.user.User;
import com.project.board.jwt.JwtUtil;
import com.project.board.user.UserRepository;
import com.project.board.security.UserDetailsImpl;
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
    public PostResponseDto createPost(PostRequestDto postRequestDto, UserDetailsImpl userDetails ) {
        // jwt토큰에서 username추출
        String username = userDetails.getUsername();

        // RequestDto -> Entity
        Post post = new Post(postRequestDto);

        // username으로 User찾아 Post에 set
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("해당 유저의 할일카드가 없습니다.")
                );
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
            throw new IllegalArgumentException("해당 할일카드가 존재하지 않습니다.");
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
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, UserDetailsImpl userDetails) {
        //  로그인한 사용자와 할일카드작성자가 같은지 대조
        Post post = checkLoginUserAndPostUser(id, userDetails);


        // Post 내용 수정
        post.update(postRequestDto);
        postRepository.save(post);

        // Entity -> Dto
        PostResponseDto postResponseDto = new PostResponseDto(post);

        return postResponseDto;
    }

    // 할일카드 완료처리
    public Boolean completePost(Long id, UserDetailsImpl userDetails) {
        //  로그인한 사용자와 할일카드작성자가 같은지 대조
        Post post = checkLoginUserAndPostUser(id, userDetails);

        // 할일카드 완료처리
        post.setComplete(true);
        postRepository.save(post);

        return true;
    }

    // 로그인한 사용자와 게시글작성자 대조
    public Post checkLoginUserAndPostUser(Long id, UserDetailsImpl userDetails){
        // ID로 할일카드 DB조회
        Post post = postRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 ID의 할일카드가 존재하지 않습니다.")
                );


        // 로그인한 사용자의 username 추출 및 해당 사용자가 작성한 할일카드 조회
        String username = userDetails.getUsername();
        List<Post> postByUsername = postRepository.findALLByUser_Username(username);
        if (postByUsername.isEmpty()){
            throw new NullPointerException("로그인한 사용자가 작성한 할일카드가 존재하지 않습니다.");
        }

        // 게시글 작성자와 로그인한 작성자가 일치하는지 검증
        String idUsername = post.getUser().getUsername();
        if (!username.equals(idUsername)){
            throw new IllegalAccessError("해당 할일카드는 작성자만 수정할 수 있습니다.");
        }

        return post;
    }

}
