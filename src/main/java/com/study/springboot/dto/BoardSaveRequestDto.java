package com.study.springboot.dto;

import com.study.springboot.entity.Board;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardSaveRequestDto {
    private String board_title; //제목
    private String board_content; //내용
    private String board_name; //작성자
    private Long board_hit; //조회수

    @Builder
    public BoardSaveRequestDto(String board_title, String board_content, String board_name, Long board_hit) {
        this.board_title = board_title;
        this.board_content = board_content;
        this.board_name = board_name;
        this.board_hit = board_hit;
    }

    public Board toEntity(){
        return Board.builder()
                .board_title(board_title)
                .board_content(board_content)
                .board_name(board_name)
                .board_hit(board_hit)
                .build();
    }
}
