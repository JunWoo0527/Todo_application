package com.project.board.user;

import com.project.board.test.CommonTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class SignupRequestDtoTest implements CommonTest {

    @Nested
    @DisplayName("유저 요청 DTO 생성")
    class createSignupRequestDTO {
        @Test
        @DisplayName("성공 케이스")
        void createSignupRequestDto_Sucess() {
            // Given
            SignupRequestDto signupRequestDto = new SignupRequestDto(TEST_USER_NAME, TEST_USER_PASSWORD);

            // When
            Set<ConstraintViolation<SignupRequestDto>> violations = validate(signupRequestDto);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("실패 케이스 : Invalid user name")
        void createSignupRequestDto_wrongName() {
            // Given
            SignupRequestDto signupRequestDto = new SignupRequestDto("Invalid user name", TEST_USER_PASSWORD);

            // When
            Set<ConstraintViolation<SignupRequestDto>> violations = validate(signupRequestDto);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message").contains("이름은 4글자 이상 10글자 이하 영소문자, 숫자만 가능합니다");

        }

        @Test
        @DisplayName("실패 케이스 : Invalid password")
        void createSignupRequestDto_wrongPassword() {
            // Given
            SignupRequestDto signupRequestDto = new SignupRequestDto(TEST_USER_NAME, "Invalid password!");

            // When
            Set<ConstraintViolation<SignupRequestDto>> violations = validate(signupRequestDto);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations).extracting("message").contains("비밀번호는 8자 이상 15자 이하 영대소문자, 숫자만 가능합니다.");

        }

    }

    private Set<ConstraintViolation<SignupRequestDto>> validate(SignupRequestDto signupRequestDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(signupRequestDto);
    }
}