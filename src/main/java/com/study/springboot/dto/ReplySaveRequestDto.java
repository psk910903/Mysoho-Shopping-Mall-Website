package com.study.springboot.dto;

import com.study.springboot.entity.Reply;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplySaveRequestDto {
    private String reply_content; // 내용
    private String reply_name; // 작성자
    private Long reply_board_idx; // 외래키

    @Builder
    public ReplySaveRequestDto(String reply_content, String reply_name, Long reply_board_idx) {
        this.reply_content = reply_content;
        this.reply_name = reply_name;
        this.reply_board_idx = reply_board_idx;
    }
    public Reply toEntity(){
        return Reply.builder()
                .replyContent(reply_content)
                .replyName(reply_name)
                .replyBoardIdx(reply_board_idx)
                .build();
    }
}
