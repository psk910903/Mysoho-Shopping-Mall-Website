<<<<<<< HEAD
package com.study.springboot.dto.review;

import com.study.springboot.entity.review.ReviewEntity;
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


=======
package com.study.springboot.dto.review;

import com.study.springboot.entity.OrderEntity;
import com.study.springboot.entity.ReviewEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
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

    @Builder
    public ReviewResponseDto(Long reviewNo, String memberId, String itemNo, Byte reviewStar, String reviewContent, String reviewImgUrl, LocalDateTime reviewDatetime, String reviewExpo) {
        this.reviewNo = reviewNo;
        this.memberId = memberId;
        this.itemNo = itemNo;
        this.reviewStar = reviewStar;
        this.reviewContent = reviewContent;
        this.reviewImgUrl = reviewImgUrl;
        this.reviewDatetime = reviewDatetime;
        this.reviewExpo = reviewExpo;
    }
    public ReviewEntity toEntity(){
        return ReviewEntity.builder()
        .reviewNo(reviewNo)
        .memberId(memberId)
        .itemNo(itemNo)
        .reviewStar(reviewStar)
        .reviewContent(reviewContent)
        .reviewImgUrl(reviewImgUrl)
        .reviewDatetime(reviewDatetime)
        .reviewExpo(reviewExpo)
        .build();
    }


>>>>>>> main
}