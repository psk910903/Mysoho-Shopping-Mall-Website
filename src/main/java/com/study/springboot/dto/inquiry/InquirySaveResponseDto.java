package com.study.springboot.dto.inquiry;

import com.study.springboot.entity.inquiry.InquiryEntity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquirySaveResponseDto {

    private String memberId;
    private Long itemNo;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryHit;

    @Builder//생성자
    public InquirySaveResponseDto(String memberId,
                                  Long itemNo,
                                  String inquiryTitle,
                                  String inquiryContent,
                                  String inquiryHit) {
        this.memberId = memberId;
        this.itemNo = itemNo;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.inquiryHit = inquiryHit;
    }
//    //dto를 entity로 바꿔주는 메서드
    public InquiryEntity toEntity(){
        return InquiryEntity.builder()
                .memberId(memberId)
                .itemNo(itemNo)
                .inquiryTitle(inquiryTitle)
                .inquiryContent(inquiryContent)
                .inquiryHit(inquiryHit)
                .build();
    }

}
