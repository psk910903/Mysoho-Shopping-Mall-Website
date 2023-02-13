package com.study.springboot.dto.review;

import com.study.springboot.entity.review.ReviewEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewSaveResponseDto {
    //dto에서 entity로 가는 통로
    //저장, 수정에 사용되는 클래스
    private String memberId;
    private String itemNo;
    private Byte reviewStar;
    private String reviewContent;
    private String reviewImgUrl;
    private String reviewExpo;


    @Builder//생성자
    public ReviewSaveResponseDto(String memberId,
                                 String itemNo,
                                 Byte reviewStar,
                                 String reviewContent,
                                 String reviewImgUrl,
                                 String reviewExpo) {
        this.memberId = memberId;
        this.itemNo = itemNo;
        this.reviewStar = reviewStar;
        this.reviewContent = reviewContent;
        this.reviewImgUrl = reviewImgUrl;
        this.reviewExpo = reviewExpo;
    }
    //dto를 entity로 바꿔주는 메서드
    public ReviewEntity toEntity(){
        return ReviewEntity.builder()
                .memberId(memberId)
                .itemNo(itemNo)
                .reviewContent(reviewContent)
                .reviewImgUrl(reviewImgUrl)
                .reviewExpo(reviewExpo)
                .reviewStar(reviewStar)
                .build();
    }
}
