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

    //관리자 상품문의 단건 조회
    @RequestMapping("/admin/inquiry/content")
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
    // 상품 문의 공개, 비공개 및 등록 결과 출력-------------------------------a
    @PostMapping("/inquiry/productInquiryWriteForm/writeAction")
    @ResponseBody
    public String productInquiryWriteFormWriteAction(InquiryResponseDto inquiryResponseDto, @RequestParam String reference) {

        //체크박스를 체크안했을 때, 반환되는 null값을 공개로 전환 ↓

        if( inquiryResponseDto.getInquirySecret() == null ){

            inquiryResponseDto.setInquirySecret("공개");
        }
        long itemNo = inquiryResponseDto.getItemNo();

        boolean result= inquiryService.save(inquiryResponseDto);
        if(!result){
            return "<script>alert('등록에 실패하였습니다');history.back();</script>";
        }
        return "<script>alert('등록되었습니다');location.href='"+ reference + "';</script>";


    }

}
