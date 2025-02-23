package com.business.i4_be.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {

    @NotBlank
    @Size(min = 4, max = 10, message = "username은 4자 이상 10자 이하여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "알파벳 소문자, 숫자가 최소 1개 이상 포함되어야 합니다.")
    private String username;

    @NotBlank
    private String nickname;

    @NotBlank
    @Size(min = 8, max = 15, message = "비밀번호는 최소 8자 이상, 15자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자가 최소 1개 이상 포함되어야 합니다.")
    private String password;

    @NotBlank
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank(message = "고객이면 USER 를, 가게 사장님이면 OWNER 를 입력해주세요.")
    private String role;
}
