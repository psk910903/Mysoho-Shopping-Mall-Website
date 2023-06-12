package com.study.springboot.dto.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class MemberLoginDto {
    @NotBlank(message = "아이디를 확인해 주세요")
    private String username;
    @NotBlank(message = "아이디/비밀번호를 확인해 주세요")
    private String password;

}
