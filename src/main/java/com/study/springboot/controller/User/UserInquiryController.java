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
import nl.captcha.Captcha;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            Long replyCount = inquiryService.countByInquiryNo(itemNo);
            replyCountList.add(replyCount);

        }
        // memberHiddenName
        String memberHiddenName = inquiryService.maskingId(memberId);

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

        String memberId = null;
        if( user!=null ) { // 회원일 때
            memberId = user.getUsername();
        }else {
            try{
                SessionUser snsUser = (SessionUser)httpSession.getAttribute("user");
                memberId = memberService.findByMemberEmail(snsUser.getEmail());
            }catch (NullPointerException e){
                System.out.println("비회원입니다.");
            }
        }
        if( memberId != null ){
            MemberResponseDto memberName = memberService.findByMemberId(memberId);
            model.addAttribute("memberName",memberName.getMemberName());
            model.addAttribute("inquiryMemberId", memberId);
            model.addAttribute("inquiryMemberPassword", memberName.getMemberPw());
        }else {// 비회원일때
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
    public String modifyAction(HttpServletRequest req, @ModelAttribute InquiryResponseDto inquiryResponseDto,
                               @RequestParam String reference, @RequestParam String captchaText){

        if(inquiryResponseDto.getInquirySecret() == null){
            inquiryResponseDto.setInquirySecret("공개");
        }

        InquiryEntity inquiryEntity = inquiryResponseDto.toModifyEntity();
        boolean modifyResult = inquiryService.inquirySave(inquiryEntity);

        Captcha captcha = (Captcha) req.getSession().getAttribute(Captcha.NAME);
        String ans = captchaText;
        if(ans!=null && !"".equals(ans)) {
            if(captcha.isCorrect(ans)) {
                if(!modifyResult){
                    return "<script>alert('수정 실패했습니다.');history.back();</script>";
                }
                return "<script>alert('수정되었습니다');location.href='"+ reference +"';</script>";
            }else {
                return "<script>alert('보안문자를 다시 입력해주세요.');history.back(); </script>";
            }
        }else{
            return "<script>alert('보안문자를 입력해주세요');history.back();</script>";
        }
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

    // 상품 문의 공개, 비공개 및 등록 결과 출력-------------------------------a
    @PostMapping("/inquiry/productInquiryWriteForm/writeAction")
    @ResponseBody
    public String productInquiryWriteFormWriteAction(HttpServletRequest req, InquiryResponseDto inquiryResponseDto,
                                                     @RequestParam String reference, @RequestParam String captchaText) {

        //체크박스를 체크안했을 때, 반환되는 null값을 공개로 전환 ↓
        if( inquiryResponseDto.getInquirySecret() == null ){

            inquiryResponseDto.setInquirySecret("공개");
        }
        boolean result= inquiryService.save(inquiryResponseDto);
        Captcha captcha = (Captcha) req.getSession().getAttribute(Captcha.NAME);
        String ans = captchaText;
        if(ans!=null && !"".equals(ans)) {
            if(captcha.isCorrect(ans)) {
                if(!result){
                    return "<script>alert('등록에 실패했습니다.');history.back();</script>";
                }
                return "<script>alert('등록되었습니다');location.href='"+ reference +"';</script>";
            }else {
                return "<script>alert('보안문자를 다시 입력해주세요.');history.back(); </script>";
            }
        }else{
            return "<script>alert('보안문자를 입력해주세요');history.back();</script>";
        }
    }

}
