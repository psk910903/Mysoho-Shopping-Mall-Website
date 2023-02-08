package com.study.springboot.dto.notice;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeUpdateRequestDto {

    private Long noticeNo;
    private String noticeType;
    private String noticeTitle;
    private String noticeContent;
    private String noticeImageUrl;

    @Builder
    public NoticeUpdateRequestDto(Long noticeNo, String noticeType, String noticeTitle, String noticeContent, String noticeImageUrl) {
        this.noticeNo = noticeNo;
        this.noticeType = noticeType;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeImageUrl = noticeImageUrl;
    }
}
