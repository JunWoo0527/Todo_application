package com.project.board.user;

import com.project.board.user.User;
import com.project.board.user.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    @Test
    @DisplayName("User 생성 테스트")
    public void test1() {
        // Given
        String username = "junwoo";
        String password = "abcd1234";
        UserRoleEnum role = UserRoleEnum.USER;

        // When
        User testUser = new User(username, password, role);

        // then
        assertEquals(username, testUser.getUsername());
        assertEquals(password, testUser.getPassword());
        assertEquals(role, testUser.getRole());
    }
}