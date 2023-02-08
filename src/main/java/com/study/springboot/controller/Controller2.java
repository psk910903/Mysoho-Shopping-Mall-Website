package com.study.springboot.controller;

import com.study.springboot.dto.notice.NoticeUpdateRequestDto;
import com.study.springboot.entity.NoticeEntity;
import com.study.springboot.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.study.springboot.dto.notice.NoticeResponseDto;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/notice")
public class Controller2 {

    private final NoticeService noticeService;

    @GetMapping("/")
    public String home(){
        return "redirect:/admin/notice/list";
    }

    @GetMapping("/list")
    public String list(Model model) {

        List<NoticeResponseDto> list = noticeService.findAll();
        model.addAttribute("list", list);

        return "admin/notice/list"; //listForm.html로 응답
    }

    @GetMapping("/content/{noticeNo}")
    public String content(@PathVariable("noticeNo") Long noticeNo, Model model) {

        NoticeResponseDto dto = noticeService.findById(noticeNo);
        if (dto == null){
            return "redirect:/admin/notice/list";
        }
        model.addAttribute("notice", dto);

        return "admin/notice/content"; //content.html로 응답
    }

    @GetMapping("/write")
    public String write(Model model) {

        NoticeResponseDto dto = NoticeResponseDto.builder()
                .noticeType("공지사항")
                .noticeTitle("")
                .noticeContent("")
                .build();

        model.addAttribute("notice",dto);

        return "admin/notice/write";//write.html로 응답
    }

    @GetMapping("/modify/{noticeNo}")
    public String update(@PathVariable("noticeNo") Long noticeNo, Model model) {

        NoticeResponseDto dto = noticeService.findById(noticeNo);
        if (dto == null){
            return "redirect:/admin/notice/list";
        }
        model.addAttribute("notice",dto);

        return "admin/notice/write"; //write.html로 응답
    }

    @PostMapping("/modifyAction")
    @ResponseBody
    public String update(NoticeUpdateRequestDto dto) {

        Boolean success = noticeService.update(dto);
        if(success) {
            return "<script>alert('글수정 완료'); location.href='/admin/notice/content/" + dto.getNoticeNo() + "';</script>";
        }else{
            return "<script>alert('글수정 실패'); history.back();</script>";
        }
    }



}
