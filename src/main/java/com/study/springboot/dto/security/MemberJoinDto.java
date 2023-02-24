package com.study.springboot.dto.security;

import com.study.springboot.entity.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class MemberJoinDto {
    @Nullable
    private Long memberNo;
    //@NotBlank(message = "user_id에 null, 빈문자열, 스페이스문자열만을 넣을 수 없습니다.")
    private String username;
    //@NotBlank(message = "user_pw에 null, 빈문자열, 스페이스문자열만을 넣을 수 없습니다.")
    private String password;
    @NotBlank
    private String memberName;
    @NotBlank
    private String memberEmail;

    @NotBlank
    private String memberRate;
    @NotBlank
    private String memberPhone;

    private Long memberMileage;//마일리지
    @NotBlank
    private String memberAddrNumber;
    @NotBlank
    private String memberAddr1;
    @NotBlank
    private String memberAddr2;

    private String memberRole;

    private String memberExited;//회원탈퇴 여부

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate memberJoinDatetime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate memberExitDatetime;
    //entity빌더 : dto를 entity로 바꿔줌. 리턴값이 entity임
    //DTO -> Entity 변환(id없음 - insert용도)
    public MemberEntity toSaveEntity() {
        return MemberEntity.builder()
                .username(username)
                .password(password)
                .memberName(memberName)
                .memberEmail(memberEmail)
                .memberRate(memberRate)
                .memberPhone(memberPhone)
                .memberMileage(memberMileage)
                .memberAddrNumber(memberAddrNumber)
                .memberAddr1(memberAddr1)
                .memberAddr2(memberAddr2)
                .memberRole(memberRole)
                .memberExited(memberExited)
                .memberJoinDatetime(memberJoinDatetime)
                .build();
    }
    //DTO -> Entity 변환(id가 있음 - update용도)
    public MemberEntity toUpdateEntity() {
        return MemberEntity.builder()
                .memberNo(memberNo)
                .username(username)
                .password(password)
                .memberName(memberName)
                .memberEmail(memberEmail)
                .memberRate(memberRate)
                .memberPhone(memberPhone)
                .memberMileage(memberMileage)
                .memberAddrNumber(memberAddrNumber)
                .memberAddr1(memberAddr1)
                .memberAddr2(memberAddr2)
                .memberRole(memberRole)
                .memberExited(memberExited)
                .memberJoinDatetime(memberJoinDatetime)
                .build();
    }

}
