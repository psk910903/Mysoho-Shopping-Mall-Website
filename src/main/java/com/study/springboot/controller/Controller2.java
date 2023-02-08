package com.study.springboot.controller;

import com.study.springboot.dto.notice.NoticeSaveRequestDto;
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
    public String list(@RequestParam(value = "findBy", required = false) String findBy,
                       @RequestParam(value = "keyword", required = false) String keyword,
                        Model model) {

        if ((findBy == null) && (keyword == null)) {
            List<NoticeResponseDto> list = noticeService.findAll();
            model.addAttribute("list", list);
            return "admin/notice/list"; //listForm.html로 응답
        }

        return  "forward:findAction?";
    }

    @GetMapping("/findAction")
    public String findAction(@RequestParam(value = "findBy", required = false) String findBy,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            Model model) {

        List<NoticeResponseDto> list = noticeService.findByKeyword(findBy, keyword);
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
    public String modify(@PathVariable("noticeNo") Long noticeNo, Model model) {

        NoticeResponseDto dto = noticeService.findById(noticeNo);
        if (dto == null){
            return "redirect:/admin/notice/list";
        }
        model.addAttribute("notice",dto);

        return "admin/notice/write"; //write.html로 응답
    }

    @PostMapping("/modifyAction")
    @ResponseBody
    public String modifyAction(NoticeUpdateRequestDto dto) {

        Boolean success = noticeService.update(dto);
        if(success) {
            return "<script>alert('글수정 완료'); location.href='/admin/notice/content/" + dto.getNoticeNo() + "';</script>";
        }else{
            return "<script>alert('글수정 실패'); history.back();</script>";
        }
    }

    @PostMapping("/writeAction")
    @ResponseBody
    public String writeAction(NoticeSaveRequestDto dto) {

        Boolean success = noticeService.save(dto);
        if(success) {
            return "<script>alert('글쓰기 완료'); location.href='/admin/notice/list';</script>";
        }else{
            return "<script>alert('글쓰기 실패'); history.back();</script>";
        }
    }

    @PostMapping("/deleteAction")
    @ResponseBody
    public String deleteAction(@RequestParam("noticeNo") Long noticeNo) {

        Boolean success = noticeService.delete(noticeNo);
        if(success) {
            return "<script>alert('글삭제 완료'); location.href='/admin/notice/list';</script>";
        }else{
            return "<script>alert('글삭제 실패'); history.back();</script>";
        }

    }


}
