package com.project.board.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupRequestDto {
    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "이름은 4글자 이상 10글자 이하 영소문자, 숫자만 가능합니다")
    private String username;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$", message = "비밀번호는 8자 이상 15자 이하 영대소문자, 숫자만 가능합니다.")
    private String password;



}