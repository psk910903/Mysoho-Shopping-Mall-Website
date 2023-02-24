package com.study.springboot.dto.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class MemberFindId {
    @NotBlank(message = "입력된 정보를 다시 한번 확인해 주세요")
    private String memberName;
    @NotBlank(message = "입력된 정보를 다시 한번 확인해 주세요")
    private String memberPhone;
}
