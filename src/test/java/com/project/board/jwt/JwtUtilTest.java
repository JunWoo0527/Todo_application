package com.project.board.jwt;

import com.project.board.test.CommonTest;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class JwtUtilTest implements CommonTest {

    @Autowired
    JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setup() {
        jwtUtil.init();
    }

    @Test
    @DisplayName("토큰 생성")
    void createToken() {
        // When
        String token = jwtUtil.createToken(TEST_USER_NAME);

        // Then
        assertNotNull(token);
    }

    @Test
    @DisplayName("쿠키에 토큰 추가 ")
    void addJwtToCookie() {
        // Given
        String token = "Test token";
        JwtUtil testJwtUtil = mock(JwtUtil.class);

        // When
        testJwtUtil.addJwtToCookie(token, response);

        // Then
        verify(testJwtUtil).addJwtToCookie(token, response);
    }

    @Test
    @DisplayName("토큰 슬라이싱")
    void substringToken() {
        // Given
        String token = "Test token";
        String BearerToken = "Bearer " + token;

        // When
        String substringToken = jwtUtil.substringToken(BearerToken);

        // Then
        assertEquals(token, substringToken);
    }

    @Nested
    @DisplayName("토큰 검증")
    class validateToken {

        @Test
        @DisplayName("토큰 검증 성공")
        void validateToken_Success() {
            // Given
            String token = jwtUtil.createToken(TEST_USER_NAME).substring(7);

            // When
            boolean isValid = jwtUtil.validateToken(token);

            // Then
            assertTrue(isValid);
        }

        @Test
        @DisplayName("토큰 검증 실패 - 유효하지 않는 토큰")
        void validateToken_Fail() {
            // Given
            String token = "Invalid token";

            // When
            boolean isValid = jwtUtil.validateToken(token);

            // Then
            assertFalse(isValid);
        }
    }

    @Test
    @DisplayName("토큰에서 사용자정보 추출")
    void getUserInfoFromToken() {
        // Given
        String token = jwtUtil.createToken(TEST_USER_NAME).substring(7);

        // When
        Claims claims = jwtUtil.getUserInfoFromToken(token);

        // Then
        assertNotNull(claims);
        assertEquals(TEST_USER_NAME, claims.getSubject());
    }

    @Test
    @DisplayName("쿠키에서 토큰 가져오기")
    void getTokenFromRequest() {
        // Given
        String token = jwtUtil.createToken(TEST_USER_NAME);
        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);

        given(request.getCookies()).willReturn(new Cookie[]{cookie});
        // When
        String jwt = jwtUtil.getTokenFromRequest(request);

        // Then
        assertNotNull(jwt);
        assertEquals(token, jwt);

    }

}