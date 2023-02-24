package com.study.springboot.controller;

import com.study.springboot.dto.inquiry.InReplyResponseDto;
import com.study.springboot.dto.inquiry.InReplySaveResponseDto;
import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.dto.inquiry.InquirySaveResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.product.ProductSearchDto;
import com.study.springboot.entity.InquiryEntity;
import com.study.springboot.entity.ProductEntity;
import com.study.springboot.entity.InReplyEntity;
import com.study.springboot.repository.InReplyRepository;
import com.study.springboot.repository.InquiryRepository;
import com.study.springboot.repository.ProductRepository;
import com.study.springboot.service.InReplyService;
import com.study.springboot.service.InquiryService;
import com.study.springboot.service.ProductService;
import com.study.springboot.service.Service5;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class Controller5 {
    public final InquiryService inquiryService;
    public final InquiryRepository inquiryRepository;
    public final InReplyService inReplyService;
    private final InReplyRepository inReplyRepository;

    private final ProductService productService; // 02 22
    private final ProductRepository productRepository; //02 22
    private final Service5 service5; // 02 23

    @GetMapping ("/admin/inquiry")
    public String inquiryMain(){
        return "redirect:/admin/inquiry/list";
    }

    @GetMapping("/admin/inquiry/list")
    public String list(Model model,
                       @RequestParam(value = "dateStart", required = false) String dateStart,
                       @RequestParam(value = "dateEnd", required = false) String dateEnd,
                       @RequestParam(value = "keyword", required = false ) String keyword,
                       @RequestParam(value = "findBy", required = false ) String findBy,
                       @RequestParam(value = "page", defaultValue = "0") int page) throws ParseException {
        Page<InquiryResponseDto> list = null;
        if(keyword == null && findBy == null && dateStart == null && dateEnd == null
                || (dateStart.equals("null")) && (dateEnd.equals("null")) && (keyword.equals("null"))
                || (dateStart.equals("")) && (dateEnd.equals("")) && (keyword.equals(""))){
            list = inquiryService.getPage(page);
        }else {
            if(!dateStart.equals("") && dateEnd.equals("")){
                list = inquiryService.findByDate(dateStart, page);
            } else if ((!dateStart.equals("")) && (!dateEnd.equals(""))) {
                list = inquiryService.findByDate(dateStart, dateEnd, page);
            } else {
                list = inquiryService.findByKeyword(findBy, keyword, page);
            }
        }
        int totalPage = list.getTotalPages();
        List<Integer> pageList = inquiryService.getPageList(totalPage, page);


        List<String> itemList = new ArrayList<>();
        for (InquiryResponseDto dto: list){
            Long itemNo = dto.getItemNo();
            ProductResponseDto itemDto = productService.findById(itemNo);
            itemList.add(itemDto.getItemName());
        }
//        model.addAttribute("count", count);
        model.addAttribute("itemList", itemList);
        model.addAttribute("list", list);
        model.addAttribute("findBy", findBy);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageList", pageList);
        model.addAttribute("dateStart", dateStart);
        model.addAttribute("dateEnd", dateEnd);

        long listCount = inquiryRepository.count();
        model.addAttribute("listCount",listCount );

        return "admin/inquiry/list";
    }

    @RequestMapping ("/admin/inquiry/content")
    public String content(@RequestParam("inquiryNo")Long inquiryNo,
                          //@RequestParam("inquiryNo")Long replyInquiryNo,
                          Model model){

        InquiryResponseDto dto = inquiryService.findById(inquiryNo);
        model.addAttribute("dto",dto);
        List<InReplyEntity> list = inReplyRepository.findAllByReplyInquiryNo(inquiryNo);
        model.addAttribute("list",list);


            Long itemNo = dto.getItemNo();
            ProductResponseDto itemDto = productService.findById(itemNo);
            String itemName = itemDto.getItemName();

        model.addAttribute("itemName", itemName);

        if (list.size() == 0) {
            model.addAttribute("nullCheck", "null");
        }

        return "admin/inquiry/content";
    }

//    리플 달기

    @RequestMapping("/admin/inquiry/writeAction")
    @ResponseBody
    public String replyWrite(InReplySaveResponseDto dto,
                             @RequestParam("replyInquiryNo") Long replyInquiryNo ){
        boolean result = inReplyService.save(dto);
        if(result) {
            return "<script>alert('답변등록 완료'); location.href='/admin/inquiry/content?inquiryNo=" + replyInquiryNo + "'; </script>";
        }else{
            return "<script>alert('답변등록 실패'); history.back();</script>";
        }
    }
    @RequestMapping("/admin/inquiry/modifyAction")
    @ResponseBody
    public String modify(InReplySaveResponseDto dto,
                         @RequestParam("replyNo") Long replyNo,
                         @RequestParam("replyInquiryNo") Long replyInquiryNo){
        String replyContent = dto.getReplyContent();
        Long replyInquiryNo1 = dto.getReplyInquiryNo();
        System.out.println("replyInquiryNo1 = " + replyInquiryNo1);
        System.out.println("replyContent = " + replyContent);
        System.out.println("replyNo = " + replyNo);
        System.out.println("replyInquiryNo = " + replyInquiryNo);
        boolean result = inReplyService.modify( dto,replyNo );
        if(result) {
            return "<script>alert('답변수정 완료'); location.href='/admin/inquiry/content?inquiryNo=" + replyInquiryNo + "'; </script>";
        }else{
            return "<script>alert('답변수정 실패'); history.back();</script>";
        }
    }

    @RequestMapping("/admin/inquiry/deleteAction")
    @ResponseBody
    public String delete(@RequestParam("replyNo") Long replyNo,
                         @RequestParam("replyInquiryNo") Long replyInquiryNo){
        boolean result = inReplyService.delete(replyNo);
        if(result){
            return "<script>alert('답변삭제 완료'); location.href='content?inquiryNo=" + replyInquiryNo + "';</script>";
        }else {
            return "<script>alert('답변삭제 실패'); history.back();</script>";
        }
    }
    // 적립금-------------------------------------------------02 21
    @GetMapping("/user/mileage") // 테스트는 Get, 끝나고 Post로 바꿈

    public String userMileage(@RequestParam("memberMileage") int memberMileage, Model model ) {
        model.addAttribute("memberMileage", memberMileage);
        return "/user/user/user-mileage";
    }
    // 쿠폰------------------------------------------------
    // 적립금과 동일, 추후에 연결작업 필요함
    @GetMapping("/user/coupon") // 테스트는 Get, 끝나고 Post로 바꿈

    public String userCoupon(@RequestParam("memberCoupon") int memberCoupon, Model model) {
        model.addAttribute("memberCoupon", memberCoupon);
        return "/user/user/coupons-mylist";
    }
    // itemNo에 따라 출력되는 상품문의 폼(혹시 몰라 주석처리) --------------------------------↓
//    @GetMapping("/inquiry/productInquiryWriteForm/{itemNo}")
//        public String inquiryProductInquiryWriteForm(@PathVariable("itemNo") Long itemNo, Model model) {
//        model.addAttribute("itemNo", itemNo);
//
//        return "/user/popup/QnA-write";
//    }
    // 상품 문의 공개, 비공개 및 등록 결과 출력-------------------------------
    @PostMapping("/inquiry/productInquiryWriteForm/writeAction")
    @ResponseBody
    public String productInquiryWriteFormWriteAction(InquirySaveResponseDto inquirySaveResponseDto) {

        //체크박스를 체크안했을 때, 반환되는 null값을 공개로 전환 ↓
        System.out.println(inquirySaveResponseDto.getInquirySecret());
        if( inquirySaveResponseDto.getInquirySecret() == null ){
            inquirySaveResponseDto.setInquirySecret("공개");
        }

        boolean result= service5.save(inquirySaveResponseDto);
        if(!result){
            return "<script>alert('등록에 실패하였습니다');history.back();</script>";
        }
        return "<script>alert('등록되었습니다');self.opener=self;window.close();</script>";
    }
    // 상품 문의작성, 작성후 페이지 종료 테스트 -----------↓
    @GetMapping("/test")
    public String test () {
        return "/user/popup/test";
    }

    // 상품 문의작성 폼(회원/비회원 나누기)------------------------------↓
    @GetMapping("/inquiry/productInquiryWriteForm/test/{itemNo}")
    public  String inquiryProductInquiryWriteFormTest(@PathVariable("itemNo") Long itemNo, Model model) {
//        String inquiryMemberId = "hong";
        String inquiryMemberId = null;

        model.addAttribute("itemNo", itemNo);
        model.addAttribute("inquiryMemberId",inquiryMemberId);

        return "/user/popup/QnA-write";
    }
}