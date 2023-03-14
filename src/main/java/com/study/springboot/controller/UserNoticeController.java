package com.study.springboot.controller;

import com.study.springboot.dto.notice.NoticeResponseDto;
import com.study.springboot.repository.NoticeRepository;
import com.study.springboot.service.AwsS3Service;
import com.study.springboot.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserNoticeController {
    private final NoticeService noticeService;
    private final AwsS3Service awsS3Service;
    private final NoticeRepository noticeRepository;

    @RequestMapping(value = "/notice", method =  {RequestMethod.GET, RequestMethod.POST})
    public String notice( @RequestParam(value = "keyword", required = false) String keyword,
                          Model model) {

        List<NoticeResponseDto> list;
        if (keyword == null) { // 검색 기능을 쓰지 않을 때
            list = noticeService.findAll();
        } else{ // 검색 기능을 쓸 때
            list = noticeService.findByKeywordTitle(keyword);
        }

        model.addAttribute("list", list);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listCount", list.size());

        return "user/category/notice";
    }

    // 공지사항 단건
    @GetMapping("/notice/{noticeNo}")
    public String noticeContent( @PathVariable("noticeNo") Long noticeNo,
                                 Model model) {

        NoticeResponseDto dto = noticeService.findById(noticeNo);
        model.addAttribute("dto", dto);

        return "user/category/notice-content";
    }

}
