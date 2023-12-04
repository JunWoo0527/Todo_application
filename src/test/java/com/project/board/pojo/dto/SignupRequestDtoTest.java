package com.project.board.pojo.dto;

import com.project.board.user.SignupRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestDtoTest {

    @Nested
    @DisplayName("SignupRequestDto 정규표현식 테스트")
    class Test1 {
        @Test
        @DisplayName("성공 케이스")
        void test1() {
            // Given
            SignupRequestDto signupRequestDto = new SignupRequestDto("junwoo123", "abcdAB1234");

            // When - Then
            assertEquals("junwoo123", signupRequestDto.getUsername());
            assertEquals("abcdAB1234", signupRequestDto.getPassword());
        }

        @Test
        @DisplayName("실패 케이스")
        void test2() {
            // Given
            SignupRequestDto signupRequestDto = new SignupRequestDto("한글은 안되지롱~", "한글은 안되요");

            // When - Then
            assertEquals("한글은 안되지롱~", signupRequestDto.getUsername());
            assertEquals("한글은 안되요", signupRequestDto.getPassword());

        }


    }

}