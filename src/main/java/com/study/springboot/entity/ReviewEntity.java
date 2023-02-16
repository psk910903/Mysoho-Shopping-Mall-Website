package com.study.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
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
    @Column(name = "review_star")
    private Byte reviewStar;
    @Column(name = "review_content")
    private String reviewContent;
    @Column(name = "review_image_url")
    private String reviewImgUrl;
    @Column(name = "review_datetime")
    @Builder.Default
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewDatetime = LocalDateTime.now();
    @Column(name = "review_exposure" )
    private String reviewExpo;


}

