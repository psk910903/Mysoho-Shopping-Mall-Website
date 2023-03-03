package com.study.springboot.entity;

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
    @Column(name = "member_no")
    private Long memberNo;
    @Column(name = "member_id")
    private String username;
    @Column(name = "member_pw")
    private String password;
    @Column(name = "member_name")
    private String memberName;
    @Column(name = "member_rate")
    private String memberRate;
    @Column(name = "member_email")
    private String memberEmail;
    @Column(name = "member_phone")
    private String memberPhone; // = member_phone = memberphone
    @Column(name = "member_mileage")
    private Long memberMileage;
    @Column(name = "member_coupon")
    private Long memberCoupon;//

    @Column(name = "member_addr_number")
    private String memberAddrNumber;
    @Column(name = "member_addr1")
    private String memberAddr1;
    @Column(name = "member_addr2")
    private String memberAddr2;
    @Column(name = "member_role")
    private String memberRole;
    @Column(name = "member_join_datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate memberJoinDatetime;
    @Column(name = "member_exit_datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate memberExitDatetime;
    @Column(name = "member_exited")
    private String memberExited;

    @Builder
    public MemberEntity(Long memberNo, String username,
                        String password, String memberName,
                        String memberRate, String memberEmail,
                        String memberPhone, Long memberMileage,
                        String memberAddrNumber, String memberAddr1,
                        String memberAddr2, String memberRole,
                        LocalDate memberJoinDatetime, LocalDate memberExitDatetime,
                        String memberExited, Long memberCoupon) { //
        this.memberNo = memberNo;
        this.username = username;
        this.password = password;
        this.memberName = memberName;
        this.memberRate = memberRate;
        this.memberEmail = memberEmail;
        this.memberPhone = memberPhone;
        this.memberMileage = memberMileage;
        this.memberCoupon = memberCoupon;
        this.memberAddrNumber = memberAddrNumber;
        this.memberAddr1 = memberAddr1;
        this.memberAddr2 = memberAddr2;
        this.memberRole = memberRole;
        this.memberJoinDatetime = memberJoinDatetime;
        this.memberExitDatetime = memberExitDatetime;
        this.memberExited = memberExited;
    }

    public void update(String password, String memberName,
                       String memberEmail, String memberPhone,
                       String memberAddrNumber, String memberAddr1,
                       String memberAddr2 ) {
        this.password = password;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberPhone = memberPhone;
        this.memberAddrNumber = memberAddrNumber;
        this.memberAddr1 = memberAddr1;
        this.memberAddr2 = memberAddr2;
    }
    public void updatePassword(String password){
        this.password = password;
    }

    public void exited(String memberExited){
        this.memberExited = memberExited;
    }
}
