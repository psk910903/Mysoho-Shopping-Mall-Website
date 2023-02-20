package com.study.springboot.dto.member;


import com.study.springboot.entity.Member.MemberEntity;
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
    private String memberName;
    private String memberId;

    private String memberPw;
    private String memberPhone;
    private String memberEmail;
    private Long memberMileage;
    private String memberAddrNumber;
    private String memberAddr1;
    private String memberAddr2;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate memberJoinDatetime;




    public MemberResponseDto(MemberEntity entity) {
        this.memberNo = entity.getMemberNo();
        this.memberName = entity.getMemberName();
        this.memberId = entity.getMemberId();
        this.memberPw = entity.getMemberPw();
        this.memberPhone = entity.getMemberPhone();
        this.memberEmail = entity.getMemberEmail();
        this.memberJoinDatetime = entity.getMemberJoinDatetime();
        this.memberMileage = entity.getMemberMileage();
        this.memberAddrNumber = entity.getMemberAddrNumber();
        this.memberAddr1 = entity.getMemberAddr1();
        this.memberAddr2 = entity.getMemberAddr2();

}
}
