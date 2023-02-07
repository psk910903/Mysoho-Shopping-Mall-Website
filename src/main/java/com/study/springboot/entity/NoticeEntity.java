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
public class NoticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_no", nullable = false)
    private Long noticeNo; //PK
    @Column(name = "notice_type", nullable = false)
    private String noticeType; //제목
    @Column(name = "notice_title", nullable = false)
    private String noticeTitle; //내용
    @Column(name = "notice_content")
    private String noticeContent; //작성자
    @Column(name = "notice_image_url")
    private Long noticeImageUrl; //조회수
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
//    public void update(String board_title, String board_content,
//                       String board_name, Long board_hit) {
//        this.boardTitle = board_title;
//        this.boardContent = board_content;
//        this.boardName = board_name;
//        this.boardHit = board_hit;
//        this.boardDate = LocalDateTime.now();
//    }
}
