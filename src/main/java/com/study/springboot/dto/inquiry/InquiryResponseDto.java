package com.study.springboot.dto.inquiry;

import com.study.springboot.entity.InquiryEntity;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class InquiryResponseDto {
    //필드 8개
    private Long inquiryNo;
    private String memberId;
    private Long itemNo;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquirySecret;
    private String inquiryHit;
    private LocalDateTime inquiryDatetime ;

    public InquiryResponseDto(InquiryEntity entity){
        this.inquiryNo = entity.getInquiryNo();
        this.memberId = entity.getMemberId();
        this.itemNo = entity.getItemNo();
        this.inquiryTitle = entity.getInquiryTitle();
        this.inquiryContent = entity.getInquiryContent();
        this.inquirySecret = entity.getInquirySecret();
        this.inquiryHit = entity.getInquiryHit();
        this.inquiryDatetime = entity.getInquiryDatetime();
    }
}
