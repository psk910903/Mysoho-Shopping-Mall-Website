package com.study.springboot.dto.inquiry;

import com.study.springboot.entity.InReplyEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Builder
public class InReplySaveResponseDto {
    //    private Long inquiryReplyNo;
    private String replyContent;
    private Long replyInquiryNo;

    public InReplyEntity toEntity() {
        return InReplyEntity.builder()
                .replyContent(replyContent)
                .replyInquiryNo(replyInquiryNo)
                .build();
    }
}