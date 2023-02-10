package com.study.springboot.entity.qna;

import com.study.springboot.entity.Reply;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name= "QnaReply")
public class QnaReplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column
    private  String reply_content;
    @Column
    private String reply_name;
    @Column
    private LocalDateTime reply_date = LocalDateTime.now();
    @Column
    private Long reply_board_idx;

    @Builder

    public QnaReplyEntity(long id, String reply_content, String reply_name,
                          LocalDateTime reply_date, Long reply_board_idx) {
        this.id = id;
        this.reply_content = reply_content;
        this.reply_name = reply_name;
        this.reply_date = reply_date;
        this.reply_board_idx = reply_board_idx;
    }
}
