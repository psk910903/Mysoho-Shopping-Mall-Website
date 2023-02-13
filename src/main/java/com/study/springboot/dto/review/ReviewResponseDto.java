package com.study.springboot.dto.review;

import com.study.springboot.entity.review.ReviewEntity;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {
    //entity에서 dto로 가는 통로
    //entity에서 dto로 바꿔져서 필드가 전부 다 있음.
    private Long reviewNo;
    private String memberId;
    private String itemNo;
    private Byte reviewStar;
    private String reviewContent;
    private String reviewImgUrl;
    private LocalDateTime reviewDatetime;
    private String reviewExpo;

    ////entity를 넣으면 dto로 바꿔주는 코드
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