package com.study.springboot.controller;

import com.study.springboot.dto.inquiry.InReplyResponseDto;
import com.study.springboot.dto.inquiry.InReplySaveResponseDto;
import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.entity.inquiry.InReplyEntity;
import com.study.springboot.repository.InReplyRepository;
import com.study.springboot.repository.InquiryRepository;
import com.study.springboot.service.InReplyService;
import com.study.springboot.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/inquiry")
public class Controller5 {
    public final InquiryService inquiryService;
    public final InquiryRepository inquiryRepository;
    public final InReplyService inReplyService;
    private final InReplyRepository inReplyRepository;

    @GetMapping ("/")
    public String inquiryMain(){
        return "redirect:/admin/inquiry/list";
    }

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "dateStart", required = false) String dateStart,
                       @RequestParam(value = "dateEnd", required = false) String dateEnd,
                       @RequestParam(value = "keyword", required = false ) String keyword,
                       @RequestParam(value = "findBy", required = false ) String findBy,
                       @RequestParam(value = "page", defaultValue = "0") int page) throws ParseException {
        Page<InquiryResponseDto> list = null;
        if(keyword == null && findBy == null && dateStart == null && dateEnd == null){
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
        model.addAttribute("list", list);
        model.addAttribute("findBy", findBy);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageList", pageList);

        long listCount = inquiryRepository.count();
        model.addAttribute("listCount",listCount );

        return "admin/inquiry/list";
    }

    @RequestMapping ("/content")
    public String content(@RequestParam("inquiryNo")Long inquiryNo,
                          //@RequestParam("inquiryNo")Long replyInquiryNo,
                          Model model){

        InquiryResponseDto dto = inquiryService.findById(inquiryNo);
        model.addAttribute("dto",dto);
        List<InReplyEntity> list = inReplyRepository.findAllByReplyInquiryNo(inquiryNo);
        model.addAttribute("list",list);

        if (list.size() == 0) {
            model.addAttribute("nullCheck", "null");
        }

        return "admin/inquiry/content";
    }

//    리플 달기

    @RequestMapping("/writeAction")
    @ResponseBody
    public String replyWrite(InReplySaveResponseDto dto,
                             @RequestParam("replyInquiryNo") Long replyInquiryNo ){
        boolean result = inReplyService.save(dto);
        if(result) {
            return "<script>alert('댓글쓰기 성공!'); location.href='/admin/inquiry/content?inquiryNo=" + replyInquiryNo + "'; </script>";
        }else{
            return "<script>alert('댓글쓰기 실패!'); history.back();</script>";
        }
    }
    @RequestMapping("/modifyAction")
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
            return "<script>alert('답변수정 성공'); location.href='/admin/inquiry/content?inquiryNo=" + replyInquiryNo + "'; </script>";
        }else{
            return "<script>alert('답변수정 실패'); history.back();</script>";
        }
    }

    @RequestMapping("/deleteAction")
    @ResponseBody
    public String delete(@RequestParam("replyNo") Long replyNo,
                         @RequestParam("replyInquiryNo") Long replyInquiryNo){
        boolean result = inReplyService.delete(replyNo);
        if(result){
            return "<script>alert('답변삭제 성공'); location.href='content?inquiryNo=" + replyInquiryNo + "';</script>";
        }else {
            return "<script>alert('답변삭제 실패'); history.back();</script>";
        }
    }
}