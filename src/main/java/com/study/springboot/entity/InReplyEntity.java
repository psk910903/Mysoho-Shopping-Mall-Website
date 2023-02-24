package com.study.springboot.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="inquiry_reply")
//@Builder
//@AllArgsConstructor
public class InReplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="inquiry_reply_no", nullable = false)
    private Long replyNo;
    @Column(name = "inquiry_reply_content", nullable = false)
    private String replyContent;
    @Column(name = "inquiry_no")
    private Long replyInquiryNo;
    @Column(name = "inquiry_reply_date")
    private LocalDateTime replyDatetime = LocalDateTime.now();

    @Builder
    public InReplyEntity(String replyContent, Long replyInquiryNo) {
        this.replyContent = replyContent;
        this.replyInquiryNo = replyInquiryNo;
    }

    public void update(String replyContent, Long replyInquiryNo) {
        this.replyContent = replyContent;
        this.replyInquiryNo = replyInquiryNo;
        this.replyDatetime = LocalDateTime.now();
    }

}
