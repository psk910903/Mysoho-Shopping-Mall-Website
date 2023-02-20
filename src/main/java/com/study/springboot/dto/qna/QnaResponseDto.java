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
    private String qnaCategory; // 상품유형
    private String qnaTitle; // 제목
    private String qnaContent; // 내용
    private String qnaPassword; // 비밀번호
    private String qnaName; // 작성자
    private Long qnaHit; // 조회 수
    private LocalDateTime qnaDate; // 생성일,수정일

    public QnaResponseDto(QnaEntity qnaEntity) {
        this.qnaId = qnaEntity.getQnaId();
        this.qnaCategory = qnaEntity.getQnaCategory();
        this.qnaTitle = qnaEntity.getQnaTitle();
        this.qnaContent = qnaEntity.getQnaContent();
        this.qnaPassword = qnaEntity.getQnaPassword();
        this.qnaName = qnaEntity.getQnaName();
        this.qnaHit =qnaEntity.getQnaHit();
        this.qnaDate =qnaEntity.getQnaLocalDateTime();
    }
}