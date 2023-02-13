package com.study.springboot.dto.qna;

import com.study.springboot.entity.QnaEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QnaResponseDto {
    private Long qna_id; // 번호
    private String qna_title; // 제목
    private String qna_content; // 내용
    private String qna_password; // 비밀번호
    private String qna_name; // 작성자
    private Long qna_hit= 0l; // 조회 수
    private LocalDateTime qna_date; // 생성일,수정일
//    private Long qna_itemcode; // 상품코드

    public QnaResponseDto(QnaEntity qna) {
        this.qna_id = qna.getId();
        this.qna_title = qna.getQna_title();
        this.qna_content = qna.getQna_content();
        this.qna_password = qna.getQna_password();
        this.qna_name = qna.getQna_name();
        this.qna_hit = qna.getQna_hit();
        this.qna_date =qna.getQna_date();
//        this.qna_itemcode=qna.getQna_itemcode();
    }
}
