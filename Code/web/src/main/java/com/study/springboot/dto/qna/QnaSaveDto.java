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
    private String memberId; //회원아이디 추가 02-23 이준하
    private String qnaCategory; //상품유형
    private String qnaContent; //작성내용
    private String qnaPassword; // 비밀번호
    private String qnaName; // 작성이름
    private String qnaSecret;//
    private LocalDateTime qnaDate; // 생성일, 수정일


    public QnaEntity toEntity() {

        return QnaEntity.builder()
                .qnaCategory(qnaCategory)
                .memberId(memberId) // 0223 이준하추가
                .qnaName(qnaName)
                .qnaPassword(qnaPassword)
                .qnaContent(qnaContent)
                .qnaSecret(qnaSecret)
                .qnaLocalDateTime(qnaDate)
                .build();
    }

    public QnaEntity toModifyEntity() {

        return QnaEntity.builder()
                .qnaId(qnaId)
                .qnaCategory(qnaCategory)
                .memberId(memberId)
                .qnaName(qnaName)
                .qnaPassword(qnaPassword)
                .qnaContent(qnaContent)
                .qnaSecret(qnaSecret)
                .qnaLocalDateTime(qnaDate)
                .build();
    }
}

