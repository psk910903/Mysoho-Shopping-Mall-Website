package com.study.springboot.dto.review;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public class ReviewSaveDto {
    public class ReviewResponseDto {
        private Long reviewNo;
        private String memberId;
        private String itemName;
        private Byte reviewStar;
        private String reviewContent;
        private String reviewImageUrl;
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDate reviewDatetime;
    }
}
