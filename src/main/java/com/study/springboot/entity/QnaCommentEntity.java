package com.study.springboot.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "qnacomment")
public class QnaCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long commentId; // 답변 번호
    @Column(name="comment_writer")
    private String commentWriter; // 글쓴이
    @Column(name="comment_content")
    private String commentContent; //내용
    @Column(name="comment_qna_id")
    private Long commentQnaId; // 게시글

    @Column(name="comment_date")
    private LocalDateTime commentDate = LocalDateTime.now();

    @Builder
    public QnaCommentEntity(Long commentId, String commentWriter, String commentContent, Long commentQnaId) {
        this.commentId = commentId;
        this.commentWriter = commentWriter;
        this.commentContent = commentContent;
        this.commentQnaId = commentQnaId;
    }
}

