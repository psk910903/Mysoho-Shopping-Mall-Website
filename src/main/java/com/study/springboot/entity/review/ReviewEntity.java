package com.study.springboot.entity.review;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Column(name = "review_star")
    private Byte reviewStar;
    @Column(name = "review_content")
    private String reviewContent;
    @Column(name = "review_image_url")
    private String reviewImgUrl;
    @Column(name = "review_datetime")
    private LocalDateTime reviewDatetime = LocalDateTime.now();
    @Column(name = "review_exposure" )
    private String reviewExpo;

    //처음 생성된 객체 저장에 사용됨.
    //pk는 자동 생성. 날짜는 함수 사용
    @Builder//생성자
    public ReviewEntity(String memberId,
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

    //저장된 자료를 수정할때 사용
    //dto에서 entity가 될때 날짜는 null이 됨.
    //null이 안되게 함수 사용
    public void update(String memberId,
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
        this.reviewDatetime = LocalDateTime.now();
    }


}

