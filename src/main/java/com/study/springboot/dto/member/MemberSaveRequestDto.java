package com.study.springboot.dto.member;


import com.study.springboot.entity.MemberEntity;
import com.study.springboot.entity.ProductEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MemberSaveRequestDto {
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
    private String memberRole;
    private String memberExited;

    public MemberSaveRequestDto(MemberEntity entity) {
        this.memberNo = entity.getMemberNo();
        this.memberName = entity.getMemberName();
        this.memberId = entity.getMemberId();
        this.memberPw = entity.getMemberPw();
        this.memberPhone = entity.getMemberPhone();
        this.memberEmail = entity.getMemberEmail();
        this.memberMileage = entity.getMemberMileage();
        this.memberAddrNumber = entity.getMemberAddrNumber();
        this.memberAddr1 = entity.getMemberAddr1();
        this.memberAddr2 = entity.getMemberAddr2();
    }

    public MemberEntity toUpdateEntity() {
        return MemberEntity.builder()
                .memberNo(memberNo)
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
                .build();
    }
}
