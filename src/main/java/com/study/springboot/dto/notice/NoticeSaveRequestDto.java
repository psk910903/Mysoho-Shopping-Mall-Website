package com.study.springboot.dto.notice;

import java.time.LocalDateTime;

public class NoticeSaveRequestDto {
    private String noticeType;
    private String noticeTitle;
    private String noticeContent;
    private String noticeImageUrl;

//    @Builder
//    public BoardSaveRequestDto(String board_title, String board_content, String board_name, Long board_hit) {
//        this.board_title = board_title;
//        this.board_content = board_content;
//        this.board_name = board_name;
//        this.board_hit = board_hit;
//    }

//    public Board toEntity(){
//        return Board.builder()
//                .board_title(board_title)
//                .board_content(board_content)
//                .board_name(board_name)
//                .board_hit(board_hit)
//                .build();
//    }
}
