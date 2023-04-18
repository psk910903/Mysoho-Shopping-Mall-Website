package com.study.springboot.controller.User;

import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.security.SessionUser;
import com.study.springboot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class UserProductController {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final InquiryService inquiryService;
    private final InReplyService inReplyService;
    private final MemberService memberService;
    private final HttpSession httpSession;

    //상품상세-----------------------------------------------------------------------------
    @GetMapping("/product/{itemNo}")
    public String productContent(Model model, @PathVariable(value = "itemNo") Long itemNo, @AuthenticationPrincipal User user) {
        ProductResponseDto dto = productService.findById(itemNo);
        String[] colorList = dto.getItemOptionColor().split(",");
        String[] sizeList = dto.getItemOptionSize().split(",");
        int colorCount = colorList.length;
        int sizeCount = sizeList.length;

        // member
        String memberId= null;
        MemberResponseDto memberResponseDto = null;
        if(user != null){
            memberId = user.getUsername();
            //memberResponseDto = memberService.findByMemberId(memberId);
        }else {//user가 null일때
            try{
                SessionUser snsUser = (SessionUser)httpSession.getAttribute("user");
                memberId = memberService.findByMemberEmail(snsUser.getEmail());
                //memberResponseDto = memberService.findByMemberId(memberId);
            }catch (NullPointerException e){//snsUser가 null일때
                System.out.println("비회원입니다");
            }
        }
        if ( memberId != null ){
            memberResponseDto = memberService.findByMemberId(memberId);
        }

        // 상품문의
        List<InquiryResponseDto> inquiry = inquiryService.findByItemNoList(itemNo);
        int listSize = inquiry.size();
        List<String> nameList = inquiryService.inquiryMaskingId(inquiry); //마스킹
        List<Long> inReplyCount = inReplyService.inReplyCount(inquiry);// 답변카운트 불러오기
        //리뷰
        List<ReviewResponseDto> reviewList = reviewService.findByReview(String.valueOf(itemNo));
        int listCount = reviewList.size();
        Double avgStar = reviewService.avgStar(reviewList);
        List<ReviewResponseDto> photoReviewList = reviewService.findByImgReview(String.valueOf(itemNo));
        int listImgCount = photoReviewList.size();
        List<String> reviewIdList = reviewService.maskingId(reviewList); //리뷰 아이디 마스킹
        List<String> photoReviewIdList = reviewService.maskingId(photoReviewList); //포토리뷰 아이디 마스킹

        model.addAttribute("list", reviewList); //리뷰 시작
        model.addAttribute("listCount", listCount);
        model.addAttribute("avgStar", avgStar);
        model.addAttribute("listImg",photoReviewList);
        model.addAttribute("reviewIdList", reviewIdList);
        model.addAttribute("photoReviewIdList", photoReviewIdList);
        model.addAttribute("listImgCount", listImgCount); //리뷰 끝
        model.addAttribute("namelist",nameList); //상품문의 시작
        model.addAttribute("LoginMemberId", memberId);
        model.addAttribute("inquiry",inquiry);
        model.addAttribute("listSize",listSize);
        model.addAttribute("inReplyCount", inReplyCount); //상품문의 끝
        model.addAttribute("member", memberResponseDto);
        model.addAttribute("colorCount", colorCount);
        model.addAttribute("sizeCount", sizeCount);
        model.addAttribute("colorList", colorList);
        model.addAttribute("sizeList", sizeList);
        model.addAttribute("dto", dto);
        model.addAttribute("cartList", null);
        return "user/product/content";
    }

    // 상품 대표이지미 확대
    @RequestMapping("/enlarge/{itemNo}")
    public String enlarge(Model model,@PathVariable(value = "itemNo") Long itemNo){
        String itemImageUrl = productService.findById(itemNo).getItemImageUrl();
        model.addAttribute("itemImageUrl", itemImageUrl);
        return "user/enlarge/enlargeProductImg";
    }

    // 상품 상세설명 확대
    @RequestMapping("/enlarge/content/{itemNo}")
    public String enlargeContent(Model model,@PathVariable(value = "itemNo") Long itemNo){
        String itemInfo = productService.findById(itemNo).getItemInfo();
        model.addAttribute("itemInfo", itemInfo);
        return "user/enlarge/enlargeProductInfo";
    }
}
