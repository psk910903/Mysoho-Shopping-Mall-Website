package com.study.springboot.dto.inquiry;

import com.study.springboot.entity.InReplyEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Builder
public class InReplySaveResponseDto {
    //    private Long inquiryReplyNo;
    private String replyContent;
    private Long replyInquiryNo;

    @Builder
    public InReplySaveResponseDto(String replyContent, Long replyInquiryNo) {
        this.replyContent = replyContent;
        this.replyInquiryNo = replyInquiryNo;
    }

    public InReplyEntity toEntity() {
        return InReplyEntity.builder()
                .replyContent(replyContent)
                .replyInquiryNo(replyInquiryNo)
                .build();
    }
}