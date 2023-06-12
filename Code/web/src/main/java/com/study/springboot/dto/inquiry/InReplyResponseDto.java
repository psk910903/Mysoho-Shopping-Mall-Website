package com.study.springboot.dto.inquiry;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InReplyResponseDto {
    //필드 4개
    private Long replyNo;
    private String replyContent;
    private Long replyInquiryNo;
    private LocalDateTime replyDatetime;

    public InReplyResponseDto(Long replyNo, String replyContent, Long replyInquiryNo, LocalDateTime replyDatetime) {
        this.replyNo = replyNo;
        this.replyContent = replyContent;
        this.replyInquiryNo = replyInquiryNo;
        this.replyDatetime = replyDatetime;
    }
}
