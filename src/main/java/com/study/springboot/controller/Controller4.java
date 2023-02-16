package com.study.springboot.controller;


import com.study.springboot.dto.qna.QnaCommentResponseDto;
import com.study.springboot.dto.qna.QnaCommentSaveDto;
import com.study.springboot.dto.qna.QnaSaveDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.service.QnaCommentService;
import com.study.springboot.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/qna")
public class Controller4 {

    private final QnaService qnaService;
    private final QnaCommentService qnaCommentService;

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
        return "/admin/qna/list";
    }
    //    글작성
    @PostMapping("/write")
    @ResponseBody
    public String write(QnaSaveDto dto) {
        boolean result = qnaService.save(dto.toEntity());
        if (!result) {
            return "<script>alert('글쓰기 실패');history.back();</script>";
        }
        return "<script>alert('글쓰기 성공');location.href='/admin/qna/list';</script>";
    }

    //    게시글리스트 가기

//    제목클릭시 상세페이지가기
@GetMapping("/content/{id}")
public String content(@PathVariable("id") long id, Model model) {
        qnaService.modifyHits(id);

    List<QnaCommentResponseDto> comment = qnaCommentService.findbyIdx(id);

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
    return "<script>alert('삭제 성공');location.href='/admin/qna/list';</script>";
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
        boolean result = qnaCommentService.save(dto.toEntity());
        if(!result){
            return "<script>alert('답글달기 실패');history.back();</script>";
        }
        return "<script>alert('답글달기 성공');location.href='/admin/qna/list';</script>";
    }

    @GetMapping("/comment/delete/{id}")
    @ResponseBody
    public String commentDelete(@PathVariable("id") long id){
        boolean result = qnaCommentService.delete(id);
        if(!result){
            return "<script>alert('답변삭제 실패했습니다.');history.back();</script>";
        }
        return "<script>alert('답변이 삭제되었습니다.');location.href='/admin/qna/list';</script>";
    }

    // 수정 입력폼가기
    @GetMapping("/comment/modify")
    public String commentModify(@RequestParam("id") long commentId,
                              @RequestParam("qnaId") long qnaId,
                              Model model){
        List<QnaResponseDto> list = qnaService.findbyid(qnaId);
        model.addAttribute("qna",list);
        model.addAttribute("qnaAnswer",qnaCommentService.findbyid(commentId));
        return "/admin/qna/modify";
        //return "/admin/qna/test";
    }
  @PostMapping("/answer/modify") //comment/modify/action
  @ResponseBody
    public String qnaAnswerModify(QnaCommentSaveDto dto){
      boolean modify = qnaCommentService.modify(dto.toModifyEntity());
//      dto.toModifyEntity()
      if(!modify){
          return "<script>alert('답변 수정 실패');history.back();</script>";
      }
      return "<script>alert('답변 수정 성공');location.href='/admin/qna/list';</script>";
  }

//  선택삭제

    @GetMapping("/selectDelete")
    public String selectDelete (@RequestParam("qnaNo") String qnaNo){
        qnaService.selectDelete(qnaNo);
        return "redirect:/admin/qna/list";
    }

}
//qnaList