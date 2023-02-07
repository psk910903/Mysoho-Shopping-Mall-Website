package com.study.springboot.controller;

import com.study.springboot.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.study.springboot.dto.notice.NoticeResponseDto;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/notice")
public class Controller2 {

    private final NoticeService noticeService;

    @RequestMapping("/")
    public String home(){
        return "redirect:/admin/notice/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<NoticeResponseDto> list = noticeService.findAll();
        model.addAttribute("list", list);
        return "admin/notice/list"; //listForm.html로 응답
    }

    @RequestMapping("/content")
    public String contentForm(@RequestParam("noticeNo") Long noticeNo, Model model) {

        NoticeResponseDto dto = noticeService.findById(noticeNo);
        model.addAttribute("dto", dto);

        return "admin/notice/content"; //contentForm.html로 응답
    }

}
