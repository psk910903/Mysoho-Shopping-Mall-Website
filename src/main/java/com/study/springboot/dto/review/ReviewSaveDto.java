package com.study.springboot.dto.review;

import com.study.springboot.entity.review.Review;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public class ReviewSaveDto {
        private Long reviewNo;
        private String memberId;
        private String itemName;
        private Byte reviewStar;
        private String reviewContent;
        private String reviewImgUrl;
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime reviewDatetime;
        private String reviewExpo;

    public Review toUpdateEntity(){
        return Review.builder()
                .reviewNo(reviewNo)
                .memberId(memberId)
                .itemName(itemName)
                .reviewStar(reviewStar)
                .reviewContent(reviewContent)
                .reviewExpo(reviewExpo)
                //.reviewDatetime(reviewDatetime)
                .reviewImgUrl(reviewImgUrl)
                .build();
    }
}
