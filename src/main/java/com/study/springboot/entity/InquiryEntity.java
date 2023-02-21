package com.study.springboot.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="inquiry")
public class InquiryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="inquiry_no", nullable = false)
    private Long inquiryNo;
    @Column(name="member_id", nullable = false)
    private String memberId;
    @Column(name = "item_no", nullable = false)
    private Long itemNo;
    @Column(name = "inquiry_title", nullable = false)
    private String inquiryTitle;
    @Column(name = "inquiry_content", nullable = false)
    private String inquiryContent;
    @Column(name = "inquiry_hit")
    private String inquiryHit;
    @Column(name = "inquiry_secret", nullable = false)
    private String inquirySecret;
    @Column(name = "inquiry_date")
    private LocalDateTime inquiryDatetime = LocalDateTime.now();

    @Builder
    public InquiryEntity(String memberId,
                         Long itemNo,
                         String inquiryTitle,
                         String inquiryContent,
                         String inquiryHit,
                         String inquirySecret) {
        this.memberId = memberId;
        this.itemNo = itemNo;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.inquiryHit = inquiryHit;
        this.inquirySecret = inquirySecret;

    }
    public void update(String memberId,
                       Long itemNo,
                       String inquiryTitle,
                       String inquiryContent,
                       String inquiryHit,
                       String inquirySec) {
        this.memberId = memberId;
        this.itemNo = itemNo;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.inquiryHit = inquiryHit;
        this.inquirySecret = inquirySecret;
        this.inquiryDatetime = LocalDateTime.now();
    }

}
