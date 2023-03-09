package com.study.springboot.dto.inquiry;

import com.study.springboot.entity.InquiryEntity;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.criteria.CriteriaBuilder;

@Getter
@Setter
@NoArgsConstructor
public class InquiryResponseDto {
    //필드 8개
    private Long inquiryNo;
    private String memberId;
    private String inquiryNickname;
    private String inquiryPassword;
    private Long itemNo;
    private String inquiryContent;
    private String inquirySecret;
//    private String inquiryHit;
    private LocalDateTime inquiryDatetime =LocalDateTime.now();

    public InquiryResponseDto(InquiryEntity entity){
        this.inquiryNo = entity.getInquiryNo();
        this.memberId = entity.getMemberId();
        this.inquiryNickname = entity.getInquiryNickname();
        this.inquiryPassword = entity.getInquiryPassword();
        this.itemNo = entity.getItemNo();
        this.inquiryContent = entity.getInquiryContent();
        this.inquirySecret = entity.getInquirySecret();
//        this.inquiryHit = entity.getInquiryHit();
        this.inquiryDatetime = entity.getInquiryDatetime();
    }

    public InquiryEntity toSaveEntity(){
        return InquiryEntity.builder()
                .memberId(memberId)
                .inquiryNickname(inquiryNickname)
                .inquiryPassword(inquiryPassword)
                .itemNo(itemNo)
                .inquiryContent(inquiryContent)
//                .inquiryHit(inquiryHit)
                .inquirySecret(inquirySecret)
                .inquiryDatetime(inquiryDatetime)
                .build();
    }

    public InquiryEntity toModifyEntity(){
        return InquiryEntity.builder()
                .inquiryNo(inquiryNo)
                .memberId(memberId)
                .inquiryNickname(inquiryNickname)
                .inquiryPassword(inquiryPassword)
                .itemNo(itemNo)
                .inquiryContent(inquiryContent)
//                .inquiryHit(inquiryHit)
                .inquirySecret(inquirySecret)
                .inquiryDatetime(inquiryDatetime)
                .build();
    }
}