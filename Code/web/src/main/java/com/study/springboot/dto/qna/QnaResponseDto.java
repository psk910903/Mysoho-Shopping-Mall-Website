package com.study.springboot.dto.qna;

import com.study.springboot.entity.QnaEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class QnaResponseDto {
    private Long qnaId; // 번호

    private String memberId; // 회원아이디
    //회원아이디 추가 02-22 이준하
    private String qnaCategory; // 상품유형
    private String qnaContent; // 내용
    private String qnaPassword; // 비밀번호
    private String qnaName; // 작성자
    private String qnaSecret;//
    private LocalDateTime qnaDate; // 생성일,수정일

    public QnaResponseDto(QnaEntity qnaEntity) {
        this.qnaId = qnaEntity.getQnaId();
        this.memberId = qnaEntity.getMemberId();
        //추가 02-22 이준하
        this.qnaCategory = qnaEntity.getQnaCategory();
        this.qnaContent = qnaEntity.getQnaContent();
        this.qnaPassword = qnaEntity.getQnaPassword();
        this.qnaName = qnaEntity.getQnaName();
        this.qnaSecret = qnaEntity.getQnaSecret();
        this.qnaDate =qnaEntity.getQnaLocalDateTime();
    }
}