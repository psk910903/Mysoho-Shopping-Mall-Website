package com.study.springboot.entity;

import lombok.Builder;
import lombok.AccessLevel;
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
    private Long noticeNo;
    @Column(name = "notice_type", nullable = false)
    private String noticeType;
    @Column(name = "notice_title", nullable = false)
    private String noticeTitle;
    @Column(name = "notice_content")
    private String noticeContent;
    @Column(name = "notice_image_url")
    private String noticeImageUrl;
    @Column(name = "notice_datetime")
    private LocalDateTime noticeDatetime = LocalDateTime.now(); // 생성일,수정일

//    @Builder
//    public NoticeEntity(String board_title, String board_content,
//                 String board_name, Long board_hit) {
//        this.boardTitle = board_title;
//        this.boardContent = board_content;
//        this.boardName = board_name;
//        this.boardHit = board_hit;
//    }
    public void update(String noticeType, String noticeTitle, String noticeContent, String noticeImageUrl) {
        this.noticeType = noticeType;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeImageUrl = noticeImageUrl;
    }
}
