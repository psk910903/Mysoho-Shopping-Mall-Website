package com.study.springboot.controller.User;

import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.security.SessionUser;
import com.study.springboot.entity.InReplyEntity;
import com.study.springboot.entity.InquiryEntity;
import com.study.springboot.entity.repository.InReplyRepository;
import com.study.springboot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserInquiryController {

    private final ProductService productService;
    private final InquiryService inquiryService;
    private final InReplyRepository inReplyRepository;
    private final MemberService memberService;
    private final HttpSession httpSession;

    //마이페이지 상품문의 리스트
    @GetMapping("/inquiry/myProductInquiries")
    public String inquiryMyProductInquiries(Model model, @AuthenticationPrincipal User user) {

        String memberId = "";
        if(user != null){
            memberId = user.getUsername();
        }else {
            SessionUser snsUser = (SessionUser)httpSession.getAttribute("user");
            memberId = memberService.findByMemberEmail(snsUser.getEmail());
        }

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

    // 상품 문의작성 폼(회원/비회원 나누기)------------------------------↓
    @GetMapping("/inquiry/productInquiryWriteForm/{itemNo}")
    public String inquiryProductInquiryWriteForm(@PathVariable("itemNo") String itemNo, Model model,
                                                 @AuthenticationPrincipal User user, @RequestParam String reference) {

        ProductResponseDto dto = productService.findById(Long.valueOf(itemNo));
        model.addAttribute("dto",dto);

        String memberId = "";
        if( user!=null ) { // 회원일 때
            memberId = user.getUsername();
        }else {
            SessionUser snsUser = (SessionUser)httpSession.getAttribute("user");
            memberId = memberService.findByMemberEmail(snsUser.getEmail());
        }
        if( !memberId.isEmpty() ){
            MemberResponseDto memberName = memberService.findByMemberId(memberId);
            String memberPassword = memberName.getMemberPw();
            model.addAttribute("memberName",memberName.getMemberName());
            model.addAttribute("inquiryMemberId", memberId);
            model.addAttribute("inquiryMemberPassword", memberPassword);
        }else {
            model.addAttribute("inquiryMemberId", null);
        }

        model.addAttribute("itemNo", itemNo);
        model.addAttribute("reference", reference);

        return "/user/popup/inquiry-write";
    }

    @GetMapping("/inquiry/delete/{id}")
    @ResponseBody
    public String inquiryDelete(@PathVariable("id")long id){
        long itemNo = inquiryService.findByItemNo(id);
        boolean deleteResult = inquiryService.delete(id);
        if(!deleteResult){
            return "<script>alert('삭제 실패');history.back();</script>";
        }
        return "<script>alert('삭제 성공'); location.href='/product/"+itemNo+"';</script>";
    }

    //비로그인 삭제 버튼 눌렀을 때
    @PostMapping("/inquiry/pw/check/action2")
    @ResponseBody
    public String pwCheckAction2(@ModelAttribute InquiryResponseDto inquiryResponseDto){

        long inquiryNo = inquiryResponseDto.getInquiryNo();
        boolean pwCheckResult = inquiryService.inquiryPwCheck(inquiryResponseDto);
        if(!pwCheckResult){
            return "<script>alert('비밀번호 확인실패'); history.back();</script>";
        }
        return "<script>location.href='/inquiry/delete/"+inquiryNo+"';</script>";
    }

    //로그인시 수정버튼눌렀을때
    @GetMapping("/inquiry/modifyForm/{num}")
    public String modifyForm(@PathVariable("num")Long num, @RequestParam String reference,
                             Model model){

        InquiryResponseDto inquiryResponseDto = inquiryService.findById(num);
        List<InReplyEntity> commentList = inReplyRepository.findAllByReplyInquiryNo(num);

        model.addAttribute("reference", reference);
        model.addAttribute("commentList", commentList);
        model.addAttribute("dto",inquiryResponseDto);

        return "/user/popup/Inquiry-modify";
    }

    @PostMapping("/inquiry/modify/action")
    @ResponseBody
    public String modifyAction(@ModelAttribute InquiryResponseDto inquiryResponseDto, @RequestParam String reference){

        if(inquiryResponseDto.getInquirySecret() == null){
            inquiryResponseDto.setInquirySecret("공개");
        }

        InquiryEntity inquiryEntity = inquiryResponseDto.toModifyEntity();
        boolean modifyResult = inquiryService.inquirySave(inquiryEntity);

        if(!modifyResult){
            return "<script>alert('수정 실패했습니다.');history.back();</script>";
        }
        return "<script>alert('수정되었습니다');location.href='"+ reference +"';</script>";
    }

    @PostMapping("/inquiry/pw/check/action")
    @ResponseBody
    public String pwCheckAction(@ModelAttribute InquiryResponseDto inquiryResponseDto, @RequestParam String reference){

        long inquiryNo = inquiryResponseDto.getInquiryNo();
        boolean pwCheckResult = inquiryService.inquiryPwCheck(inquiryResponseDto);
        if(!pwCheckResult){
            return "<script>alert('비밀번호 확인실패'); history.back();</script>";
        }
        return "<script>alert('비밀번호 확인완료.'); location.href='/inquiry/modifyForm/"+inquiryNo+"?reference=" + reference +"';</script>";
    }

}
