package com.study.springboot.dto.inquiry;

import com.study.springboot.entity.InquiryEntity;
import java.time.LocalDateTime;
import lombok.Getter;

import javax.persistence.Column;

@Getter
public class InquiryResponseDto {
    //필드 8개
    private Long inquiryNo;
    private String memberId;
    private String inquiryNickname;
    private String inquiryPassword;
    private Long itemNo;
    private String inquiryContent;
    private String inquirySecret;
    private String inquiryHit;
    private LocalDateTime inquiryDatetime ;

    public InquiryResponseDto(InquiryEntity entity){
        this.inquiryNo = entity.getInquiryNo();
        this.memberId = entity.getMemberId();
        this.inquiryNickname = entity.getInquiryNickname();
        this.inquiryPassword = entity.getInquiryPassword();
        this.itemNo = entity.getItemNo();
        this.inquiryContent = entity.getInquiryContent();
        this.inquirySecret = entity.getInquirySecret();
        this.inquiryHit = entity.getInquiryHit();
        this.inquiryDatetime = entity.getInquiryDatetime();
    }
}