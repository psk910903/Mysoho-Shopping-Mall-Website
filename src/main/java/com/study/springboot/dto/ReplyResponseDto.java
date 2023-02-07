package com.study.springboot.dto;

import com.study.springboot.entity.Reply;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplyResponseDto {
    private Long reply_idx; // PK
    private String reply_content; // 내용
    private String reply_name; // 작성자
    private LocalDateTime reply_date; // 생성일,수정일
    private Long reply_board_idx; //외래키

    public ReplyResponseDto(Reply entity) {
        this.reply_idx = entity.getReplyIdx();
        this.reply_content = entity.getReplyContent();
        this.reply_name = entity.getReplyName();
        this.reply_board_idx = entity.getReplyBoardIdx();
        this.reply_date = entity.getReplyDate();
    }
}
