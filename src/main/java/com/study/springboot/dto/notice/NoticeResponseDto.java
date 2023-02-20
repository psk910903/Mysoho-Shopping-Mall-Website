package com.study.springboot.dto.notice;

import com.study.springboot.entity.NoticeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeResponseDto {

    private Long noticeNo;
    private String noticeType;
    private String noticeTitle;
    private String noticeContent;
    private LocalDateTime noticeDatetime;

    @Builder
    public NoticeResponseDto(Long noticeNo, String noticeType, String noticeTitle, String noticeContent, LocalDateTime noticeDatetime) {
        this.noticeNo = noticeNo;
        this.noticeType = noticeType;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeDatetime = noticeDatetime;
    }

    public NoticeResponseDto(NoticeEntity entity) {
        this.noticeNo = entity.getNoticeNo();
        this.noticeType = entity.getNoticeType();
        this.noticeTitle = entity.getNoticeTitle();
        this.noticeContent = entity.getNoticeContent();
        this.noticeDatetime = entity.getNoticeDatetime();
    }
}
