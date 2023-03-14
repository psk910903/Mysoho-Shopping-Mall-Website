package com.study.springboot.controller;

import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.repository.NoticeRepository;
import com.study.springboot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserInquiryController {
    private final NoticeService noticeService;
    private final AwsS3Service awsS3Service;
    private final NoticeRepository noticeRepository;
    private final ProductService productService;
    private final InquiryService inquiryService;

    //마이페이지 상품문의 리스트
    @GetMapping("/inquiry/myProductInquiries")
    public String inquiryMyProductInquiries(Model model, @AuthenticationPrincipal User user) {

        String memberId = user.getUsername();

        List<InquiryResponseDto> inquiryList = inquiryService.findByMemberId(memberId);
        List<ProductResponseDto> itemList = new ArrayList<>();
        List<Long> replyCountList = new ArrayList<>();

        for(InquiryResponseDto inquiryDto : inquiryList) {
            // itemList
            Long itemNo = inquiryDto.getItemNo();
            ProductResponseDto productDto = productService.findById(itemNo);
            itemList.add(productDto);

            // replyCountList
            Long replyCount = inquiryService.countByInquiryNo(inquiryDto.getInquiryNo());
            replyCountList.add(replyCount);

        }

        // memberHiddenName
        String memberName = memberId;
        String memberHiddenName;

        if (memberName.length() <= 2){
            memberHiddenName = memberName;
        }
        else{
            memberHiddenName = memberName.substring(0,2);
            for (int i=0; i<memberName.length()-2; i++) memberHiddenName += "*";
        }

        model.addAttribute("itemList", itemList);
        model.addAttribute("inquiryList", inquiryList);
        model.addAttribute("replyCountList", replyCountList);
        model.addAttribute("memberHiddenName", memberHiddenName);

        return "user/user/myProductInquiries";
    }

    //마이페이지 상품문의 삭제
    @PostMapping("/inquiry/myProductInquiries/deleteAction")
    @ResponseBody
    public String inquiryMyProductInquiriesDeleteAction(@RequestParam Long inquiryNo) {

        Boolean success = inquiryService.inquiryDelete(inquiryNo);
        if(success) {
            return "<script>alert('삭제되었습니다.'); location.href='/inquiry/myProductInquiries';</script>";
        }else{
            return "<script>alert('삭제 실패했습니다.'); history.back();</script>";
        }
    }

}
