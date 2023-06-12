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
    @Column(name="member_id")
    private String memberId;
    @Column(name="inquiry_nickname")
    private String inquiryNickname;
    @Column(name="inquiry_password")
    private String inquiryPassword;
    @Column(name = "item_no", nullable = false)
    private Long itemNo;
    @Column(name = "inquiry_content", nullable = false)
    private String inquiryContent;

    @Column(name = "inquiry_secret", nullable = false)
    private String inquirySecret;
    @Column(name = "inquiry_date")
    private LocalDateTime inquiryDatetime = LocalDateTime.now();

    @Builder
    public InquiryEntity(Long inquiryNo,
                         String memberId,
                         String inquiryNickname,
                         String inquiryPassword,
                         Long itemNo,
                         String inquiryContent,
                         String inquirySecret,
                         LocalDateTime inquiryDatetime
    ) {
        this.inquiryNo = inquiryNo;
        this.memberId = memberId;
        this.inquiryNickname = inquiryNickname;
        this.inquiryPassword = inquiryPassword;
        this.itemNo = itemNo;
        this.inquiryContent = inquiryContent;
        this.inquirySecret = inquirySecret;
        this.inquiryDatetime = inquiryDatetime;

    }
    public void update(String memberId,
                       String inquiryNickname,
                       String inquiryPassword,
                       Long itemNo,
                       String inquiryContent,
                       String inquirySecret) {
        this.memberId = memberId;
        this.inquiryNickname = inquiryNickname;
        this.inquiryPassword = inquiryPassword;
        this.itemNo = itemNo;
        this.inquiryContent = inquiryContent;
        this.inquirySecret = inquirySecret;
        this.inquiryDatetime = LocalDateTime.now();
    }

}
