package com.project.board.service;

import com.project.board.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signupTestSucessful() {
        // Given
        SignupRequestDto signupRequestDto = new SignupRequestDto("junwoo", "abcd1234");
        String username = signupRequestDto.getUsername();

        UserService userService = new UserService(userRepository, passwordEncoder);

        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        // When
        userService.signup(signupRequestDto);

        // Then
        assertNotNull(userRepository);
    }

    @Test
    @DisplayName("회원가입 실패 테스트 (중복회원 : IllegalArgumentException)")
    void signupTestFailed() {
        // Given
        SignupRequestDto signupRequestDto = new SignupRequestDto("junwoo", "abcd1234");
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        User user = new User(username, password, UserRoleEnum.USER);
        UserService userService = new UserService(userRepository, passwordEncoder);

        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

        // When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.signup(signupRequestDto);
        });

        // Then
        assertEquals("이미 가입한 회원입니다.", exception.getMessage());
    }



}