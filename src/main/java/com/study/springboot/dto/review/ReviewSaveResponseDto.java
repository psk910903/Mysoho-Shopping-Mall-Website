package com.study.springboot.dto.review;

import com.study.springboot.entity.review.ReviewEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ReviewSaveResponseDto {
    private Long reviewNo;
    private String memberId;
    private String itemNo;
    private Byte reviewStar;
    private String reviewContent;
    private String reviewImgUrl;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewDatetime = LocalDateTime.now();
    private String reviewExpo;



    public ReviewEntity toUpdateEntity(){
        return ReviewEntity.builder()
                .reviewNo(reviewNo)
                .memberId(memberId)
                .itemNo(itemNo)
                .reviewStar(reviewStar)
                .reviewContent(reviewContent)
                .reviewExpo(reviewExpo)
                //.reviewDatetime(reviewDatetime)
                .reviewImgUrl(reviewImgUrl)
                .build();
    }

    public ReviewEntity toSaveEntity(){
        return ReviewEntity.builder()
                //.reviewNo(reviewNo)
                .memberId(memberId)
                .itemNo(itemNo)
                .reviewStar(reviewStar)
                .reviewContent(reviewContent)
                .reviewExpo(reviewExpo)
                //.reviewDatetime(reviewDatetime)
                .reviewImgUrl(reviewImgUrl)
                .build();
    }


    public ReviewEntity toEntity(){
        return ReviewEntity.builder()
                .itemNo(itemNo)
                .reviewContent(reviewContent)
                .reviewStar(reviewStar)
                .reviewImgUrl(reviewImgUrl)
                .memberId(memberId)
                .reviewExpo(reviewExpo)
                .build();
    }

    @Builder
    public ReviewSaveResponseDto(String memberId, String itemNo,
                                 Byte reviewStar, String reviewContent,
                                 String reviewImgUrl, String reviewExpo) {
        this.memberId = memberId;
        this.itemNo = itemNo;
        this.reviewStar = reviewStar;
        this.reviewContent = reviewContent;
        this.reviewImgUrl = reviewImgUrl;
        this.reviewExpo = reviewExpo;
    }

}
