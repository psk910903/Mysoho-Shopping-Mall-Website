package com.study.springboot.entity;

import com.study.springboot.dto.review.ReviewResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "review")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_no")
    private Long reviewNo;
    @Column(name = "member_id")
    private String memberId;
    @Column(name = "item_no")
    private String itemNo;
    @Column(name = "orderCode")
    private String orderCode;
    @Column(name = "review_star")
    private Byte reviewStar;
    @Column(name = "review_content")
    private String reviewContent;
    @Column(name = "review_image_url")
    private String reviewImgUrl;
    @Column(name = "review_datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewDatetime = LocalDateTime.now();
    @Column(name = "review_exposure" )
    private String reviewExpo;

    @Builder
    public ReviewEntity(Long reviewNo, String memberId, String itemNo,String orderCode, Byte reviewStar, String reviewContent, String reviewImgUrl, LocalDateTime reviewDatetime, String reviewExpo) {
        this.reviewNo = reviewNo;
        this.memberId = memberId;
        this.itemNo = itemNo;
        this.orderCode = orderCode;
        this.reviewStar = reviewStar;
        this.reviewContent = reviewContent;
        this.reviewImgUrl = reviewImgUrl;
        this.reviewDatetime = reviewDatetime;
        this.reviewExpo = reviewExpo;
    }
}

