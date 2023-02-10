package com.study.springboot.dto.notice;

import com.study.springboot.entity.NoticeEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class NoticeResponseDto {

    private Long noticeNo;
    private String noticeType;
    private String noticeTitle;
    private String noticeContent;
    private String noticeImageUrl;
    private LocalDateTime noticeDatetime;

    @Builder
    public NoticeResponseDto(Long noticeNo, String noticeType, String noticeTitle, String noticeContent, String noticeImageUrl, LocalDateTime noticeDatetime) {
        this.noticeNo = noticeNo;
        this.noticeType = noticeType;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeImageUrl = noticeImageUrl;
        this.noticeDatetime = noticeDatetime;
    }

    public NoticeResponseDto(NoticeEntity entity) {
        this.noticeNo = entity.getNoticeNo();
        this.noticeType = entity.getNoticeType();
        this.noticeTitle = entity.getNoticeTitle();
        this.noticeContent = entity.getNoticeContent();
        this.noticeImageUrl = entity.getNoticeImageUrl();
        this.noticeDatetime = entity.getNoticeDatetime();
    }
}
