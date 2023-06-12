package com.study.springboot.dto.review;

import com.study.springboot.entity.ReviewEntity;
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
    private String reviewExpo;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewDatetime;

    public ReviewEntity toUpdateEntity(){
        return ReviewEntity.builder()
                .reviewNo(reviewNo)
                .memberId(memberId)
                .itemNo(itemNo)
                .reviewStar(reviewStar)
                .reviewContent(reviewContent)
                .reviewExpo(reviewExpo)
                .reviewImgUrl(reviewImgUrl)
                .reviewDatetime(reviewDatetime)
                .build();
    }

    public ReviewEntity toSaveEntity(){
        return ReviewEntity.builder()
                .memberId(memberId)
                .itemNo(itemNo)
                .reviewStar(reviewStar)
                .reviewContent(reviewContent)
                .reviewExpo(reviewExpo)
                .reviewImgUrl(reviewImgUrl)
                .reviewDatetime(reviewDatetime)
                .build();
    }

}
