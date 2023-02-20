package com.study.springboot.dto.notice;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeUpdateRequestDto {

    private Long noticeNo;
    private String noticeType;
    private String noticeTitle;
    private String noticeContent;

    @Builder
    public NoticeUpdateRequestDto(Long noticeNo, String noticeType, String noticeTitle, String noticeContent) {
        this.noticeNo = noticeNo;
        this.noticeType = noticeType;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
    }
}
