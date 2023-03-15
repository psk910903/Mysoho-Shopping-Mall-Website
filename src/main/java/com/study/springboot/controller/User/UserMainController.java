package com.study.springboot.controller.User;

import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.entity.repository.NoticeRepository;
import com.study.springboot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class UserMainController {

    private final ProductService productService;
    private final NoticeRepository noticeRepository;

    //홈페이지 -----------------------------------------------------------------------------
    @GetMapping("/")
    public String mySoho(Model model) {
        List<ProductResponseDto> bestItem = productService.findByItem(6);
        List<ProductResponseDto> list = productService.findByItem(9);

        List<ProductResponseDto> sellCount = productService.SortItem(list, "판매량");
        List<ProductResponseDto> lowPrice = productService.SortItem(list, "낮은가격");
        List<ProductResponseDto> HighReview = productService.SortItem(list, "리뷰");
        List<ProductResponseDto> HighGrade = productService.SortItem(list, "평점");
        model.addAttribute("sellCount", sellCount);
        model.addAttribute("lowPrice", lowPrice);
        model.addAttribute("HighReview", HighReview);
        model.addAttribute("HighGrade", HighGrade);
        model.addAttribute("bestItem", bestItem);
        model.addAttribute("list", list);
        model.addAttribute("latestNotice", noticeRepository.findLatestNotice());


        return "/user/category/home";
    }

    //상품검색 -----------------------------------------------------------------------------
    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam(value = "keyword", required = false) String keyword) {
        List<ProductResponseDto> list = productService.findByKeyword(keyword);

        List<ProductResponseDto> sellCount = productService.SortItem(list, "판매량");
        List<ProductResponseDto> lowPrice = productService.SortItem(list, "낮은가격");
        List<ProductResponseDto> HighReview = productService.SortItem(list, "리뷰");
        List<ProductResponseDto> HighGrade = productService.SortItem(list, "평점");
        model.addAttribute("sellCount", sellCount);
        model.addAttribute("lowPrice", lowPrice);
        model.addAttribute("HighReview", HighReview);
        model.addAttribute("HighGrade", HighGrade);

        int count = list.size();
        model.addAttribute("list", list);
        model.addAttribute("keyword", keyword);
        model.addAttribute("count", count);
        return "/user/category/search";
    }

    // 카테고리-----------------------------------------------------------------------------

    @GetMapping("/plan/item/{category}")
    public String planItem(Model model,@PathVariable(value = "category") String category) {
        List<ProductResponseDto> list = productService.findByCategory(category);

        model.addAttribute("list", list);
        model.addAttribute("category", category);
        return "/user/category/content";
    }

    // 이용약관 -----------------------------------------------------------------------------
    @GetMapping("/terms/terms/service")
    public String termsTermsService() {

        return "/user/popup/pop-page1";
    }

    @GetMapping("/terms/terms/privacy")
    public String termsTermsPrivacy() {

        return "/user/popup/pop-page2";
    }

    @GetMapping("/terms/terms/service/order")
    public String termsTermsServiceOrder() {

        return "/user/popup/pop-page3";
    }

    @GetMapping("/terms/terms/policy/order")
    public String termsTermsPolicyOrder() {

        return "/user/popup/pop-page4";
    }

    //로그인 폼
    @GetMapping("/user/login")
    public String login() {
        return "user/user/userlogin";
    }

    //가입 폼
    @GetMapping("/user/join")
    public String join() {
        return "user/user/userjoin";
    }






}
