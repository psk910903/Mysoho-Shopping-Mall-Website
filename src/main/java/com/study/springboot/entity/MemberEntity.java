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
    private Long member_no;
    private String member_id;
    private String member_pw;
    private String member_name;
    private String member_email;
    private String member_phone;
    private Long member_mileage;
    private String member_address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate member_join_datetime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate member_exit_datetime;
    private String member_exited;


    @Builder
    public MemberEntity(String member_name, String member_email, String member_phone, String member_address
    ,Long member_no) {
        this.member_name = member_name;
        this.member_email = member_email;
        this.member_phone = member_phone;
        this.member_address = member_address;
        this.member_no = member_no;
    }

    public void update( String member_name, String member_phone, String member_address,
                    String member_email,Long member_no){
        this.member_name = member_name;
        this.member_email = member_email;
        this.member_phone = member_phone;
        this.member_address = member_address;
        this.member_no = member_no;
    }



}
