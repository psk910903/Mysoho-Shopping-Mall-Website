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
    private Long member_no;
    private String member_name;
    private String member_id;

    private String member_pw;
    private String member_phone;
    private String member_email;
    private Long member_mileage;
    private String member_address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate member_join_datetime;




    public MemberResponseDto(MemberEntity entity) {
        this.member_no = entity.getMember_no();
        this.member_name = entity.getMember_name();
        this.member_id = entity.getMember_id();
        this.member_pw = entity.getMember_pw();
        this.member_phone = entity.getMember_phone();
        this.member_email = entity.getMember_email();
        this.member_join_datetime = entity.getMember_join_datetime();
        this.member_mileage = entity.getMember_mileage();
        this.member_address = entity.getMember_address();

}
}
