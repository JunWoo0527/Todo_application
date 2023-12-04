package com.project.board.test;


import com.project.board.user.User;
import com.project.board.user.UserRoleEnum;

public interface CommonTest {
    String AUTHORIZATION_HEADER = "Authorization";
    String ANOTHER_PREFIX = "another-";
    Long TEST_USER_ID = 1L;
    Long TEST_ANOTHER_USER_ID = 2L;
    String TEST_USER_NAME = "username";
    String TEST_USER_PASSWORD = "password";

    User TEST_USER = new User(TEST_USER_NAME, TEST_USER_PASSWORD, UserRoleEnum.USER);
    User TEST_ANOTHER_USER = new User(ANOTHER_PREFIX + TEST_USER_NAME,
            ANOTHER_PREFIX + TEST_USER_PASSWORD);

}
