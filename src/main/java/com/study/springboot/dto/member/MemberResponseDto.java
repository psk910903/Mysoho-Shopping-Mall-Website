package com.study.springboot.dto.member;


import com.study.springboot.entity.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MemberResponseDto {
    private Long memberNo;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberRate;
    private String memberEmail;
    private String memberPhone;
    private Long memberMileage;
    private String memberAddrNumber;
    private String memberAddr1;
    private String memberAddr2;
    private String memberExited;
    private String memberRole;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate memberJoinDatetime;

    public MemberResponseDto(MemberEntity entity) {
        this.memberNo = entity.getMemberNo();
        this.memberName = entity.getMemberName();
        this.memberId = entity.getUsername();
        this.memberPw = entity.getPassword();
        this.memberPhone = entity.getMemberPhone();
        this.memberEmail = entity.getMemberEmail();
        this.memberJoinDatetime = entity.getMemberJoinDatetime();
        this.memberMileage = entity.getMemberMileage();
        this.memberAddrNumber = entity.getMemberAddrNumber();
        this.memberAddr1 = entity.getMemberAddr1();
        this.memberAddr2 = entity.getMemberAddr2();
        this.memberExited = entity.getMemberExited();
        this.memberRate = entity.getMemberRate();
        this.memberRole = entity.getMemberRole();
    }
}
