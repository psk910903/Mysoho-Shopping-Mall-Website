package com.study.springboot.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberEmail;

    private String memberPhone; // = member_phone = memberphone
    private Long memberMileage;
    private String memberAddrNumber;
    private String memberAddr1;
    private String memberAddr2;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate memberJoinDatetime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate member_exit_datetime;
    private String member_exited;


    @Builder
    public MemberEntity(String memberName, String memberEmail, String memberPhone, String memberAddrNumber,
                            String memberAddr1, String memberAddr2, Long member_no) {
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberPhone = memberPhone;
        this.memberAddrNumber = memberAddrNumber;
        this.memberAddr1 = memberAddr1;
        this.memberAddr2 = memberAddr2;
        this.memberNo = member_no;
    }

    public void modify(String memberName, String memberPhone, String memberAddrNumber,
                       String memberAddr1, String memberAddr2,
                       String memberEmail, Long memberNo){
        this.memberName = memberName;
        this.memberPhone = memberPhone;
        this.memberAddrNumber = memberAddrNumber;
        this.memberAddr1 = memberAddr1;
        this.memberAddr2 = memberAddr2;
        this.memberEmail = memberEmail;
        this.memberNo = memberNo;
    }



}
