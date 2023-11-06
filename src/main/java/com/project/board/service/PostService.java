package com.project.board.service;

import com.project.board.dto.PostRequestDto;
import com.project.board.dto.PostResponseDto;
import com.project.board.entity.Post;
import com.project.board.repository.PostRepository;
import org.springframework.stereotype.Service;


import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 게시글 저장
    public PostResponseDto createPost(PostRequestDto postRequestDto) {

        // RequestDto -> Entity
        Post post = new Post(postRequestDto);

        // Entity ID 부여 및 postList에 저장
        Long maxId = postRepository.getPostList().size() > 0 ? Collections.max(postRepository.getPostList().keySet()) + 1 : 1;
        post.setId(maxId);
        postRepository.getPostList().put(post.getId(), post);

        // Entity -> ResponseDto
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    // 게시글 출력
    public PostResponseDto printPost(Long id) {
        PostResponseDto postResponseDto = new PostResponseDto(postRepository.getPostList().get(id));
        return  postResponseDto;
    }

    // 게시글 전체 출력
    public List<PostResponseDto> printAllPost() {

        // Map to List
        List<PostResponseDto> resopnseList = postRepository.getPostList().values().stream()
                .map(PostResponseDto::new).toList();

        // 내림차순 정렬
        List<PostResponseDto> resopnseListDesc = resopnseList.stream()
                .sorted(Comparator.comparing(PostResponseDto::getDate).reversed()).toList();
        return resopnseListDesc;
    }

    // 게시글 수정
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto) {
        // 해당 게시글이 DB에 존재하는지 확인
        if (postRepository.getPostList().containsKey(id)) {

            // RequestDto -> Entity
            Post post = new Post(postRequestDto);

            // Password가 일치하는지 확인
            if (Objects.equals(postRepository.findById(id).getPassword(), post.getPassword())) {

                // 해당 게시글 수정
                post.setId(id);

                Date date = new Date(Calendar.getInstance().getTimeInMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                post.setDate(simpleDateFormat.format(date));

                postRepository.findById(id).update(post);

                // Entity -> ResponseDto
                PostResponseDto postResponseDto = new PostResponseDto(post);

                return postResponseDto;
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }
    }


    // 게시글 삭제
    public String deletePost(Long id, String password) {

        // 해당 게시글이 DB에 존재하는지 확인
        if (postRepository.getPostList().containsKey(id)) {

            // Password가 일치하는지 확인
            if (Objects.equals(postRepository.findById(id).getPassword(), password)) {

                // 해당 게시글 삭제
                postRepository.getPostList().remove(id);

                return "삭제되었습니다.";
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }




    }








}
