package com.study.springboot.dto.member;

import com.study.springboot.entity.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class MemberSaveDto2 {
    private Long memberNo; // id
    @Size(min=5,max=20,message = "아이디는 5~20자 입니다.")
    @NotBlank(message = "아이디를 입력해주세요.")
    private String memberId; //회원 아이디
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 8~20자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String memberPw; //회원 패스워드
    @NotBlank(message = "이름을 입력해 주세요.")
    private String memberName;// 회원이름
    private String memberRate;// 회원등급
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String memberEmail;//회원이메일
    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Size(min=5,max=20,message = "올바른 휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^\\d{2,3}\\d{3,4}\\d{4}$", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String memberPhone;//회원 연락처
    private Long memberMileage;// 회원마일리지
    @NotBlank(message = "우편번호를 입력해주세요.")
    private String memberAddrNumber;//회원 우편번호
    @NotBlank(message = "주소를 입력해주세요.")
    private String memberAddr1; // 회원 기본주소
    @NotBlank(message = "주소를 입력해주세요.")
    private String memberAddr2; // 회원 나머지주소
    private String memberRole; // 회원역할
    private String memberExited; // 회원 탈퇴날자
    private LocalDate memberJoinDatetime; // 회원 가입날자

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .memberId(memberId)
                .memberPw(memberPw)
                .memberName(memberName)
                .memberRate(memberRate)
                .memberEmail(memberEmail)
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
