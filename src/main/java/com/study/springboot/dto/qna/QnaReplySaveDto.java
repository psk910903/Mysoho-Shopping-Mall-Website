package com.study.springboot.dto.qna;

import com.study.springboot.entity.qna.QnaReplyEntity;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaReplySaveDto {
    private Long id; // 글번호
    private String reply_title; // 제목
    private String reply_content; // 내용
    private String reply_password; // 비밀번호
    private String reply_name; // 작성자
    private LocalDateTime reply_date; // 생성일,수정일
    private Long reply_board_idx; // 댓글 번호

    public QnaReplyEntity toSaveReplyEntity(){
        return QnaReplyEntity.builder()
                .reply_content(reply_content)
                .reply_name(reply_name)
                .reply_date(reply_date)
                .reply_board_idx(reply_board_idx)
                .build();
    }

}
