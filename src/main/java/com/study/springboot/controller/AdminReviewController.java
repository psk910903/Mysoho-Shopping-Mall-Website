package com.study.springboot.controller;

import com.study.springboot.entity.ReviewEntity;
import com.study.springboot.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AdminReviewController {
    final ReviewService reviewService;

    // 관리자페이지 리뷰 상세 정보 admin/review/content?reviewNo=12
    @RequestMapping("/admin/review/content/{reviewNo}")
    public String adminReviewContent(Model model, @PathVariable(value = "reviewNo") Long reviewNo){
        ReviewEntity review = reviewService.findById(reviewNo);

        model.addAttribute("review", review);
        return "/admin/review/content";
    }
}
