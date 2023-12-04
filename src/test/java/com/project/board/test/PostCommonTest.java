package com.project.board.test;

import com.project.board.post.Post;
import com.project.board.post.PostRequestDto;

public interface PostCommonTest extends CommonTest{

    Long TEST_POST_ID = 1L;
    String TEST_POST_TITLE = "title";
    String TEST_POST_CONTENT = "content";

    PostRequestDto TEST_POST_REQUEST_DTO = new PostRequestDto(TEST_POST_TITLE, TEST_POST_CONTENT);

    Post TEST_POST = new Post(TEST_POST_REQUEST_DTO);
    Post TEST_ANOTHER_POST = new Post(
            new PostRequestDto(ANOTHER_PREFIX + TEST_POST_TITLE, ANOTHER_PREFIX + TEST_POST_CONTENT)
    );
}
