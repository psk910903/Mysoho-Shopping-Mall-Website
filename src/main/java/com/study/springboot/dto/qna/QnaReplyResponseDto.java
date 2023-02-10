package com.study.springboot.dto.qna;

import com.study.springboot.entity.qna.QnaReplyEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QnaReplyResponseDto {
    private Long id; // 글번호
    private String reply_title; // 제목
    private String reply_content; // 내용
    private String reply_password; // 비밀번호
    private String reply_name; // 작성자
    private LocalDateTime reply_date; // 생성일,수정일
    private Long reply_board_idx;

    public QnaReplyResponseDto(QnaReplyEntity qnaReply) {
        this.id = id;
        this.reply_content = qnaReply.getReply_content();
        this.reply_name = qnaReply.getReply_name();
        this.reply_date = qnaReply.getReply_date();
        this.reply_board_idx = qnaReply.getReply_board_idx();
    }


}
