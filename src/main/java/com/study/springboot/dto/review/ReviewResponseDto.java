package com.study.springboot.dto.review;

import com.study.springboot.entity.ReviewEntity;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {

    private Long reviewNo;
    private String memberId;
    private String itemNo;
    private Byte reviewStar;
    private String reviewContent;
    private String reviewImgUrl;
    private LocalDateTime reviewDatetime;
    private String reviewExpo;

    public ReviewResponseDto(ReviewEntity entity){
        this.reviewNo = entity.getReviewNo();
        this.itemNo = entity.getItemNo();
        this.reviewContent = entity.getReviewContent();
        this.reviewExpo = entity.getReviewExpo();
        this.reviewImgUrl = entity.getReviewImgUrl();
        this.reviewDatetime = entity.getReviewDatetime();
        this.reviewStar = entity.getReviewStar();
        this.memberId = entity.getMemberId();
    }


}