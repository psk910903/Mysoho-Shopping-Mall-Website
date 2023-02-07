package com.study.springboot.dto;

import com.study.springboot.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {

    private Long board_idx; // PK
    private String board_title; // 제목
    private String board_content; // 내용
    private String board_name; // 작성자
    private Long board_hit; // 조회 수
    private LocalDateTime board_date; // 생성일,수정일

    public BoardResponseDto(Board entity) {
        this.board_idx = entity.getBoardIdx();
        this.board_title = entity.getBoardTitle();
        this.board_content = entity.getBoardContent();
        this.board_name = entity.getBoardName();
        this.board_hit = entity.getBoardHit();
        this.board_date = entity.getBoardDate();
    }

}
