package com.study.springboot.dto.qna;

import com.study.springboot.entity.QnaCommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class QnaCommentSaveDto {
    private Long commentId; // PK
    private String commentWriter; // 작성자
    private String commentContent; //내용
    private Long commentQnaId; // 게시물 번호
    private LocalDateTime commentDate; // 생성일


    public QnaCommentEntity toEntity() {
        return QnaCommentEntity.builder()
                .commentWriter(commentWriter)
                .commentContent(commentContent)
                .commentQnaId(commentQnaId)
                .build();
    }
    public QnaCommentEntity toModifyEntity(){
        return QnaCommentEntity.builder()
                .commentId(commentId)
                .commentWriter(commentWriter)
                .commentContent(commentContent)
                .commentQnaId(commentQnaId)
                .build();
    }
}
