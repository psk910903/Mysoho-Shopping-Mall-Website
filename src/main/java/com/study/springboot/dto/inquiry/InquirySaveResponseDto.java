package com.study.springboot.dto.inquiry;

import com.study.springboot.entity.InquiryEntity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquirySaveResponseDto {

    private String memberId;
    private String inquiryNickname;
    private String inquiryPassword;
    private Long itemNo;
    private String inquiryContent;
//    private String inquiryHit;
    private String inquirySecret;

    @Builder//생성자
    public InquirySaveResponseDto(String memberId,
                                  String inquiryNickname,
                                  String inquiryPassword,
                                  Long itemNo,
                                  String inquiryContent,
                                  String inquirySecret
    ) {
        this.memberId = memberId;
        this.inquiryNickname = inquiryNickname;
        this.inquiryPassword = inquiryPassword;
        this.itemNo = itemNo;
        this.inquiryContent = inquiryContent;
        this.inquirySecret = inquirySecret;
    }
    //    //dto를 entity로 바꿔주는 메서드
    public InquiryEntity toEntity(){
        return InquiryEntity.builder()
                .memberId(memberId)
                .inquiryNickname(inquiryNickname)
                .inquiryPassword(inquiryPassword)
                .itemNo(itemNo)
                .inquiryContent(inquiryContent)
                .inquirySecret(inquirySecret)
                .build();
    }
}