package com.study.springboot.dto.qna;

import com.study.springboot.entity.QnaEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QnaSaveDto {
    private Long qnaId; // 숫자
    private String qnaCategory; //상품유형
    private String qnaTitle; // 제목
    private String qnaContent; //작성내용
    private String qnaPassword; // 비밀번호
    private String qnaName; // 작성이름
    private String qnaSecret;//비밀글 여부
    private Long qnaHit; //조회수
    private LocalDateTime qnaDate; // 생성일, 수정일



    public QnaEntity toEntity() {

        return QnaEntity.builder()
                .qnaCategory(qnaCategory)
                .qnaName(qnaName)
                .qnaTitle(qnaTitle)
                .qnaPassword(qnaPassword)
                .qnaContent(qnaContent)
                .qnaSecret(qnaSecret)
                .qnaHit(0l)
                .qnaLocalDateTime(qnaDate)
                .build();
    }

}
