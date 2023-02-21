package com.study.springboot.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="notice")
public class NoticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_no", nullable = false)
    private Long noticeNo; // PK
    @Column(name = "notice_type", nullable = false)
    private String noticeType; // 타입(공지사항, 이벤트)
    @Column(name = "notice_title", nullable = false)
    private String noticeTitle; // 제목
    @Column(name = "notice_content")
    private String noticeContent; // 내용
    @Column(name = "notice_datetime")
    private LocalDateTime noticeDatetime = LocalDateTime.now(); // 생성일, 수정일

    @Builder
    public NoticeEntity(String noticeType, String noticeTitle, String noticeContent) {
        this.noticeType = noticeType;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
    }

    public void update(String noticeType, String noticeTitle, String noticeContent) {
        this.noticeType = noticeType;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
    }
}
