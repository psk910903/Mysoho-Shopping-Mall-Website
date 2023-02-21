package com.study.springboot.controller;


import com.study.springboot.dto.qna.QnaCommentResponseDto;
import com.study.springboot.dto.qna.QnaCommentSaveDto;
import com.study.springboot.dto.qna.QnaSaveDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.repository.QnaRepository;
import com.study.springboot.service.QnaCommentService;
import com.study.springboot.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/qna")
public class Controller4 {

    private final QnaService qnaService;
    private final QnaCommentService qnaCommentService;
    private final QnaRepository qnaRepository;

    @GetMapping("/")
    public String qnaHome(){
        return "redirect:/admin/qna/list";
    }

    @GetMapping("/list")
    public String list(@RequestParam(value = "keywordType", required = false) String keywordType,
                         @RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam(value = "dateStart", required = false) String dateStart,
                         @RequestParam(value = "dateEnd", required = false) String dateEnd,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         Model model) throws ParseException {
        Page<QnaResponseDto> list;

        int totalPage;
        List<Integer> pageList;


        if ((keywordType == null) && (keyword == null) && (dateStart == null) && (dateEnd == null)) { // 찾기기능을 쓰지않을때
            list = qnaService.findAll(page);

        } else {

            //오늘, 어제, 1주일, 1개월 검색
            if ((!dateStart.equals("")) && (dateEnd.equals(""))) {
                list = qnaService.findByDate(dateStart, page);

                //기간 검색
            } else if ((!dateStart.equals("")) && (!dateEnd.equals(""))) {
                list = qnaService.findByDate(dateStart, dateEnd, page);
            } else {
                //검색 키워드가 있을 때
                list = qnaService.findByKeyword(keywordType, keyword, page);
            }

        }
        totalPage = list.getTotalPages();
        pageList = qnaService.getPageList(totalPage, page);
        model.addAttribute("keywordType", keywordType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("qnalist", list);
        model.addAttribute("pageList", pageList);

        long listCount = qnaRepository.count();
        model.addAttribute("listCount", listCount);
        return "/admin/qna/list";
    }

@GetMapping("/content/{id}")
public String content(@PathVariable("id") long id, Model model) {
        qnaService.modifyHits(id);

    List<QnaCommentResponseDto> comment = qnaCommentService.findbyIdx(id);

    if (comment.size() == 0) {
        model.addAttribute("nullCheck", "null");
    }

    model.addAttribute("comment",comment);
    model.addAttribute("qna", qnaService.findbyid(id));

    return "/admin/qna/content";
}

@GetMapping("/delete/{id}")
@ResponseBody
public String delete(@PathVariable("id") Long id){
    boolean delete = qnaService.delete(id);
    if(!delete){
        return "<script>alert('삭제 실패');history.back();</script>";
    }
    return "<script>alert('삭제 완료');location.href='/admin/qna/list';</script>";
}
//    쓰기폼 가기(셀렉트 테스트용-사용자페이지에서해야함)
    @GetMapping("/write")
    public String qnaWrite(){
        return "/admin/qna/write";
    }

//    여기서부터 코멘트----------------------------------------------------------------------

    @PostMapping("/comment/write")
    @ResponseBody
    public String commentWrite(QnaCommentSaveDto dto){
        Long commentQnaId = dto.getCommentQnaId();
        boolean result = qnaCommentService.save(dto.toEntity());
        if(!result){
            return "<script>alert('답변등록 실패');history.back();</script>";
        }
        return "<script>alert('답변등록 완료'); location.href='/admin/qna/content/" + commentQnaId + "'; </script>";
    }

    @GetMapping("/comment/delete/{id}")
    @ResponseBody
    public String commentDelete(@PathVariable("id") long id){
        boolean result = qnaCommentService.delete(id);
        if(!result){
            return "<script>alert('답변삭제 실패');history.back();</script>";
        }
        return "<script>alert('답변삭제 완료');location.href='/admin/qna/list';</script>";
    }

    //수정
    @PostMapping("/comment/modify")
    @ResponseBody
    public String commentModify(QnaCommentSaveDto dto){
        Long commentQnaId = dto.getCommentQnaId();
        boolean result = qnaCommentService.save(dto.toModifyEntity());
        if(!result){
            return "<script>alert('답변수정 실패');history.back();</script>";
        }
        return "<script>alert('답변수정 완료'); location.href='/admin/qna/content/" + commentQnaId + "'; </script>";
    }

//  선택삭제
    @GetMapping("/select/delete")
    public String selectDelete (@RequestParam("qnaNo") String qnaNo){
        qnaService.selectDelete(qnaNo);
        return "redirect:/admin/qna/list";
    }

}
//qnaList