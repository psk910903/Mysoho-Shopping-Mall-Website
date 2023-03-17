package com.study.springboot.controller.admin;

import com.study.springboot.dto.inquiry.InReplySaveResponseDto;
import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.entity.InReplyEntity;
import com.study.springboot.entity.repository.InReplyRepository;
import com.study.springboot.entity.repository.InquiryRepository;
import com.study.springboot.service.*;
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
public class AdminInquiryController {

    public final InquiryService inquiryService;
    public final InquiryRepository inquiryRepository;
    public final InReplyService inReplyService;
    private final InReplyRepository inReplyRepository;
    private final ProductService productService;
    private final NoticeService noticeService;

    @GetMapping("/admin/inquiry")
    public String inquiryMain(){
        return "redirect:/admin/inquiry/list";
    }
    //관리자 상품문의 목록
    @GetMapping("/admin/inquiry/list")
    public String list(Model model,
                       @RequestParam(value = "dateStart", required = false) String dateStart,
                       @RequestParam(value = "dateEnd", required = false) String dateEnd,
                       @RequestParam(value = "keyword", required = false ) String keyword,
                       @RequestParam(value = "findBy", required = false ) String findBy,
                       @RequestParam(value = "page", defaultValue = "0") int page) throws ParseException {

        Page<InquiryResponseDto> list;
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
        List<Integer> pageList = noticeService.getPageList(totalPage, page);


        List<String> itemList = new ArrayList<>();
        for (InquiryResponseDto dto: list){
            Long itemNo = dto.getItemNo();
            ProductResponseDto itemDto = productService.findById(itemNo);
            itemList.add(itemDto.getItemName());
        }
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

    //관리자 상품문의 단건 조회
    @RequestMapping("/admin/inquiry/content")
    public String content(@RequestParam("inquiryNo")Long inquiryNo,
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

    //답글 달기
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


}
