package com.study.springboot.dto.member;


import com.study.springboot.entity.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MemberSaveRequestDto {
    private Long memberNo;
    private String memberId;
    private String memberPw;
    //private String memberNewPw;
    private String memberName;
    private String memberRate;
    private String memberEmail;
    private String memberPhone;
    private Long memberMileage;
    private Long memberCoupon;
    private String memberAddrNumber;
    private String memberAddr1;
    private String memberAddr2;
    private String memberRole;
    private String memberExited;
    private LocalDate memberJoinDatetime;

    public MemberEntity toUpdateEntity() {
        return MemberEntity.builder()
                .memberNo(memberNo)
                .username(memberId)
                .password(memberPw)
                .memberName(memberName)
                .memberRate(memberRate)
                .memberEmail(memberEmail)
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
    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .username(memberId)
                .password(memberPw)
                .memberName(memberName)
                .memberRate(memberRate)
                .memberEmail(memberEmail)
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
