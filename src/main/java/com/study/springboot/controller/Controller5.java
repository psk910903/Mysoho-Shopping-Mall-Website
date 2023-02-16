//package com.study.springboot.controller;
//
//import com.study.springboot.dto.qna.QnaReplySaveDto;
//import com.study.springboot.dto.qna.QnaResponseDto;
//import com.study.springboot.dto.qna.QnaSaveDto;
//import com.study.springboot.service.QnaReplyService;
//import com.study.springboot.service.QnaService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@Controller
//@RequestMapping("/admin/qna")
//public class Controller5 {
//
//    private final QnaService qnaService;
//    private final QnaReplyService qnaReplyService;
//    //
//    @GetMapping("/QnaContent/{idx}")
//    public String content(@PathVariable("idx") long idx, Model model){
//        List<QnaResponseDto> list = qnaService.findbyIdx(idx);
//        model.addAttribute("list",list);
//        return "/admin/qna/QnaContent";
//    }
//
//
//    //
//    @GetMapping("/QnaList")
//    public String list(Model model){
//        List<QnaResponseDto> list = qnaService.findAll();
//        model.addAttribute("list",list);
//        return "/admin/qna/QnaList";
//    }
//    @GetMapping("/QnaWrite")
//    public String write(){return "/admin/qna/QnaWrite";}
//
//    //글쓰기 버튼을 눌렀을 때,경고창을 띄워주는 페이지
//    @PostMapping("/QnaWriteDo")
//    @ResponseBody
//    public String writedo(QnaSaveDto qnaSaveDto){
//        boolean result= qnaService.save(qnaSaveDto.toSaveEntity());
//        if(!result){
//            return "<script>alert('글쓰기 실패');history.back();</script>";
//        }
//        return "<script>alert('글쓰기 성공');location.href='/admin/qna/QnaList';</script>";    }
//
//    // 삭제하기
//    @GetMapping("/delete")
//    @ResponseBody
//    public String QnaDelete(Long qna_id) {
//        boolean result =  qnaService.QnaDelete(qna_id);
//        if(!result){
//            return "<script>alert('삭제 실패');history.back();</script>";
//        }
//        return "<script>alert('삭제 성공');location.href='/admin/qna/QnaList';</script>";
//    }
//
//    @GetMapping("/QnaContent")
//    public String QnaContent() {
//        return "admin/qna/QnaContent";
//    }
//
//    //↓ 댓글(잠시 주석처리)
//     //댓글달기 버튼을 눌렀을 때, 뜨는 경고창 페이지
//
//    @PostMapping("/QnaReplyWriteDo")
//    @ResponseBody
//    public String replywritedo(QnaReplySaveDto qnaReplySaveDto){
//        boolean result= qnaReplyService.save(qnaReplySaveDto.toSaveReplyEntity());
//        if(!result){
//            return "<script>alert('댓글쓰기 실패');history.back();</script>";
//        }
//        return "<script>alert('댓글쓰기 성공');location.href='/admin/qna/QnaContent';</script>";    }
//
//
//
//
//}
