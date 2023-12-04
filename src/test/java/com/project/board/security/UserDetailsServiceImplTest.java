package com.project.board.security;

import com.project.board.user.User;
import com.project.board.user.UserRepository;
import com.project.board.user.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    static UserRepository userRepository;

    @Test
    @DisplayName("유저 이름으로 유저 조회 성공")
    void loadUserByUsername() {
        // Given
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

        String username = "username";
        User testUser = new User(username, "1234", UserRoleEnum.USER);


        UserDetails testUserDetails = new UserDetailsImpl(testUser);

        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertEquals(testUserDetails.getUsername(), userDetails.getUsername());
        assertEquals(testUserDetails.getPassword(), userDetails.getPassword());
        assertEquals(testUserDetails.getAuthorities(), userDetails.getAuthorities());
    }

    @Test
    @DisplayName("유저 이름으로 유저 조회 실패(UsernameNotFoundException")
    void loadUserByUsernameWhenThrow() {
        // Given
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

        String username = "username";

        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.empty());

        // When
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

        // Then
        assertEquals("Not Found " + username, exception.getMessage());
    }
}