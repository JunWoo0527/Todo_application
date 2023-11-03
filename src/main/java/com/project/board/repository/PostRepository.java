package com.project.board.repository;

import com.project.board.dto.PostRequestDto;
import com.project.board.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PostRepository {

    private final Map<Long, Post> postList = new HashMap<>();


    // 게시글 ID 생성 및 저장
    public Post savePost(Post post) {
        // Memo Max ID Check
        // memoList 사이즈가 0보다 크다면 메모리스트이 키셋 의 가장큰값의 +1 해서 넣고 0보다 크지않다면 1을 넣어라
        Long maxId = postList.size() > 0 ? Collections.max(postList.keySet()) + 1 : 1;
        post.setId(maxId);
        postList.put(post.getId(), post);
        return post;
    }

    // 게시글 ID로 게시글 불러오기
    public Post findById(Long id) {
        return postList.get(id);
    }

    // 저장된 게시글 모두 출력
    public List<Post> printAll() {
        return new ArrayList<>(postList.values());
    }

    // 저장된 게시글 업데이트
    public Long updatePost(Long postId, PostRequestDto requestDto) {
        if (postList.containsKey(postId)) {
            Post post = findById(postId);

            post.update(requestDto);

            return post.getId();

        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }
    }






}
