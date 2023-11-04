package com.project.board.repository;


import com.project.board.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PostRepository {

    private final Map<Long, Post> postList = new HashMap<>();



    // 게시글 ID로 게시글 불러오기
    public Post findById(Long id) {
        return postList.get(id);
    }


    public Map<Long, Post> getPostList() {
        return postList;
    }







}
