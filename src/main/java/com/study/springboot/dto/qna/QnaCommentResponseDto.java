package com.study.springboot.dto.qna;

import com.study.springboot.entity.QnaCommentEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
//QnaResponseCommentDto -> QnaCommentResponseDto
@Getter
@Setter
public class QnaCommentResponseDto { //QnaCommentResponseDto
    private Long commentId; // 답글 번호
    private String commentWriter; // 글쓴이
    private String commentContent; // 내용
    private long commentQnaId; // 문의글 번호
    private LocalDateTime commentDate; // 생성일

    public QnaCommentResponseDto(QnaCommentEntity entity){
        this.commentId = entity.getCommentId();
        this.commentWriter = entity.getCommentWriter();
        this.commentContent = entity.getCommentContent();
        this.commentQnaId = entity.getCommentQnaId();
        this.commentDate = entity.getCommentDate();
    }
}
