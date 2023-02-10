package com.study.springboot.entity.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
//@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_no")
    private Long reviewNo;
    @Column(name = "member_id")
    private String memberId;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "review_star")
    private Byte reviewStar;
    @Column(name = "review_content")
    private String reviewContent;
    @Column(name = "review_image_url")
    private String reviewImgUrl;
    @Column(name = "review_datetime")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime reviewDatetime;
    @Column(name = "review_exposure" )
    private String reviewExpo;


}
//        review_no INT AUTO_INCREMENT NOT NULL PRIMARY KEY, -- 고유키
//        member_id VARCHAR(255) NOT NULL, -- 아이디(회원) / 비회원은 후기를 달수없음
//        item_code VARCHAR(255) NOT NULL UNIQUE, -- 상품 코드(UUID포맷-32자리)
//        review_star TINYINT NOT NULL,  -- 별점
//        review_content TEXT NULL,  -- 상품후기
//        review_image_url TEXT NULL, -- 이미지
//        review_datetime DATETIME DEFAULT NOW() -- 작성시간
//        );
