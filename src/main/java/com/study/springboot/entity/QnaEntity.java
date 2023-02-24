package com.study.springboot.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name="qna")
public class QnaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaId; //PK
    @Column(name="member_id")
    private String memberId; // 회원아이디
    // 회원아이디추가
    @Column(name="qna_category")
    private String qnaCategory; //카테고리

    @Column(name="qna_name")
    private String qnaName;  // 작성자 이름

    @Column(name="qna_title")
    private String qnaTitle; // 작성 제목

    @Column(name="qna_password")
    private String qnaPassword; // 비밀번호

    @Column(name="qna_content")
    private String qnaContent; // 내용

    @Column(name="qna_secret")
    private int qnaSecret;//비밀글 여부 String-> int 으로수정 0223 이준하

    @Column(name="qna_hit")
    private Long qnaHit=0l; // 조회수

    @Column(name="qna_local_date_time")
    private LocalDateTime qnaLocalDateTime = LocalDateTime.now(); //생성일 ,수정일
    @Builder
    public QnaEntity(Long qnaId,String memberId, String qnaCategory,
                     String qnaName, String qnaTitle,
                     String qnaPassword, String qnaContent,
                     int qnaSecret, //String-> int 으로수정 0223 이준하
                     Long qnaHit, LocalDateTime qnaLocalDateTime
                     ) {
        this.qnaId = qnaId;
        this.memberId =memberId;
        this.qnaCategory = qnaCategory;
        this.qnaName = qnaName;
        this.qnaTitle = qnaTitle;
        this.qnaPassword = qnaPassword;
        this.qnaContent = qnaContent;
        this.qnaSecret =qnaSecret;
    }


}




