package com.study.springboot.dto.qna;


import com.study.springboot.entity.qna.QnaEntity;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class QnaSaveDto {
    private Long id; // 글번호
    private String qna_title; // 제목
    private String qna_content; // 내용
    private String qna_password; // 비밀번호
    private String qna_name; // 작성자
    private Long qna_hit =0l; // 조회 수
    private LocalDateTime qna_date; // 생성일,수정일
    private Long qna_itemcode; // 상품코드

    public QnaEntity toSaveEntity(){
        return QnaEntity.builder()
                .qna_title(qna_title)
                .qna_content(qna_content)
                .qna_password(qna_password)
                .qna_name(qna_name)
                .qna_date(qna_date)
                .qna_hit(qna_hit)
//                .qna_itemcode(qna_itemcode)
                .build();
    }
    public QnaEntity toUpdateEntity(){
        return QnaEntity.builder()
                .id(id)
                .qna_title(qna_title)
                .qna_content(qna_content)
                .qna_password(qna_password)
                .qna_name(qna_name)
                .qna_date(qna_date)
                .qna_hit(qna_hit)
//                .qna_itemcode(qna_itemcode)
                .build();
    }
}
