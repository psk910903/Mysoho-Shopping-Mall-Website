package com.study.springboot.dto.security;

import com.study.springboot.entity.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class MemberJoinDto {
    @Nullable
    private Long memberNo;
    @NotBlank//(message = "user_id에 null, 빈문자열, 스페이스문자열만을 넣을 수 없습니다.")
    @Pattern( regexp = "^[a-zA-z0-9_-]{5,20}$", message = "아이디를 양식에 맞게 작성해 주세요")
    private String username;
    @NotBlank//(message = "user_pw에 null, 빈문자열, 스페이스문자열만을 넣을 수 없습니다.")
    @Pattern( regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,20}" , message = "패스워드를 양식에 맞게 작성해 주세요" )
    private String password;
    @Pattern( regexp = "^[가-힣]{2,5}$", message = "이름을 양식에 맞게 작성해 주세요")
    @NotBlank
    private String memberName;
    @NotBlank
    private String memberEmail;

    @NotBlank
    private String memberRate;
    @NotBlank
    @Pattern( regexp = "^[0-9]{10,11}$", message = "전화번호를 양식에 맞게 작성해 주세요")
    private String memberPhone;

    private Long memberMileage;//마일리지

    private Long memberCoupon;
    @NotBlank
    private String memberAddrNumber;
    //@NotBlank
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
                .memberCoupon(memberCoupon)
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
                .memberCoupon(memberCoupon)
                .memberAddrNumber(memberAddrNumber)
                .memberAddr1(memberAddr1)
                .memberAddr2(memberAddr2)
                .memberRole(memberRole)
                .memberExited(memberExited)
                .memberJoinDatetime(memberJoinDatetime)
                .build();
    }

}
