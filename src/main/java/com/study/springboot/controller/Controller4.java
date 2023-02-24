package com.study.springboot.controller;


import com.study.springboot.dto.member.MemberSaveDto2;
import com.study.springboot.dto.qna.QnaCommentResponseDto;
import com.study.springboot.dto.qna.QnaCommentSaveDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.dto.qna.QnaSaveDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.entity.QnaEntity;
import com.study.springboot.repository.QnaRepository;
import com.study.springboot.service.QnaCommentService;
import com.study.springboot.service.QnaService;
import com.study.springboot.service.Service4;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Member;
import java.text.ParseException;
import java.util.List;
@RequiredArgsConstructor
@Controller
@RequestMapping("/")
//   /admin/qna

public class Controller4 {
    private final Service4 service4;

    private final QnaService qnaService;
    private final QnaCommentService qnaCommentService;
    private final QnaRepository qnaRepository;

    @GetMapping("/admin/qna")
    public String qnaHome(){
        return "redirect:/admin/qna/list";
    }

    @GetMapping("admin/qna/list")
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

@GetMapping("admin/qna/content/{id}")
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

@GetMapping("admin/qna/delete/{id}")
@ResponseBody
public String delete(@PathVariable("id") Long id){
    boolean delete = qnaService.delete(id);
    if(!delete){
        return "<script>alert('삭제 실패');history.back();</script>";
    }
    return "<script>alert('삭제 완료');location.href='/admin/qna/list';</script>";
}
//    쓰기폼 가기(셀렉트 테스트용-사용자페이지에서해야함)
//    @GetMapping("/write")
//    public String qnaWrite(){
//        return "/admin/qna/write";
//    }

//    여기서부터 코멘트----------------------------------------------------------------------

    @PostMapping("admin/qna/comment/write")
    @ResponseBody
    public String commentWrite(QnaCommentSaveDto dto){
        Long commentQnaId = dto.getCommentQnaId();
        boolean result = qnaCommentService.save(dto.toEntity());
        if(!result){
            return "<script>alert('답변등록 실패');history.back();</script>";
        }
        return "<script>alert('답변등록 완료'); location.href='/admin/qna/content/" + commentQnaId + "'; </script>";
    }

    @GetMapping("admin/qna/comment/delete/{id}")
    @ResponseBody
    public String commentDelete(@PathVariable("id") long id){
        boolean result = qnaCommentService.delete(id);
        if(!result){
            return "<script>alert('답변삭제 실패');history.back();</script>";
        }
        return "<script>alert('답변삭제 완료');location.href='/admin/qna/list';</script>";
    }

    //수정
    @PostMapping("admin/qna/comment/modify")
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
    @GetMapping("admin/qna/select/delete")
    public String selectDelete (@RequestParam("qnaNo") String qnaNo){
        qnaService.selectDelete(qnaNo);
        return "redirect:/admin/qna/list";
    }

    //-------------여기서부터 사용자페이지------------------------------------------
    // 회원가입 시작
    // 회원가입폼가기
    @GetMapping("user/join")
    public String userjoin(){
        return "user/user/userjoin";
    }
    // 홈화면가기
    @GetMapping("/")
    public String home(){
        return "user/category/home";
    }
    // 회원가입폼 join/action 받기
    @PostMapping("join/action")
    @ResponseBody
    public String joinAction(@Valid MemberSaveDto2 dto2, BindingResult bindingResult){
        if( bindingResult.hasErrors() ) {
            //DTO에 설정한 message값을 가져온다.
            String detail = bindingResult.getFieldError().getDefaultMessage();
            return "<script>alert('"+ detail +"'); history.back();</script>";
        }
        MemberEntity memberEntity =dto2.toEntity();
        boolean saveResult = service4.save(memberEntity);
        if(!saveResult){
            return "<script>alert('회원가입 실패'); histroy.back();</script>";
        }
        return "<script>alert('축하합니다 회원가입 성공하셨습니다'); location.href='/';</script>";
    }

    // ajax 아이디중복체크 요청처리
    @GetMapping("id/check")
    @ResponseBody
    public String idCheck(@RequestParam("user_id")String userId){
       List<MemberEntity>list = service4.findByUserId(userId);
       if(list.size() == 0){
           return "0"; // 중복안됨
       }else{
           return "1"; // 중복됨
       }
    }//회원가입 끝

// Qna 시작

// 게시판에서 문의작성눌렀을떄 글쓰는 폼 들어가기
    @GetMapping("user/qna/writeForm")
    public String userQnaWrite(){
        return "/user/popup/QnA-write";
    }

// Qna 검색액션받기랑 게시판가기
@GetMapping("user/category/qna")
public String qnaSearchAction(@RequestParam(value ="keyword", required = false) String keyword,
                              Model model){
    List<QnaResponseDto> list;
        if(keyword ==null){// 검색기능 없을 때
            list = service4.findEvery();
            model.addAttribute("list",list);
            return "/user/category/QnA";
        }else{ //검색기능 있을 때
             list = service4.keyword(keyword);
        }
    System.out.println(list);
        model.addAttribute("list",list);
    return "/user/category/QnA";
    }

    @PostMapping("user/qna/write")
    @ResponseBody
    public String userQnaWriteAction(QnaSaveDto saveDto){
//        System.out.println("saveDto = " + saveDto);
        QnaEntity qnaEntity= saveDto.toEntity();
        boolean qnasave = service4.qnasave(qnaEntity);
        if(!qnasave){
            return "<script>alert('등록 실패 하였습니다'); histroy.back();</script>";
        }
        return "<script>alert('등록에 성공 하였습니다.'); location.href='/user/category/qna';</script>";
    }
    //리스트로감


    // 글 삭제
    @GetMapping("user/qna/delete/{id}")
    @ResponseBody
    public String userDelete (@PathVariable("id")long id){
        boolean delete = service4.delete(id);
        if(!delete){
            return "<script>alert('삭제 실패 하였습니다'); histroy.back();</script>";
        }
        return "<script>alert('삭제 성공 하였습니다.'); location.href='/user/category/qna';</script>";
    }


    //qna 리스트가기
}
//qnaList