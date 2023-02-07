package com.study.springboot.dto.notice;

import com.study.springboot.entity.NoticeEntity;

import java.time.LocalDateTime;


public class NoticeResponseDto {

    private Long noticeNo;
    private String noticeType;
    private String noticeTitle;
    private String noticeContent;
    private Long noticeImageUrl;
    private LocalDateTime noticeDatetime;

    public NoticeResponseDto(NoticeEntity entity) {
        this.noticeNo = entity.getNoticeNo();
        this.noticeType = entity.getNoticeType();
        this.noticeTitle = entity.getNoticeTitle();
        this.noticeContent = entity.getNoticeContent();
        this.noticeImageUrl = entity.getNoticeImageUrl();
        this.noticeDatetime = entity.getNoticeDatetime();
    }
}
