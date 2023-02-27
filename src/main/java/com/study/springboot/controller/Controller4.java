package com.study.springboot.controller;


import com.study.springboot.dto.qna.QnaCommentResponseDto;
import com.study.springboot.dto.qna.QnaCommentSaveDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.dto.qna.QnaSaveDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.entity.QnaEntity;
import com.study.springboot.repository.QnaRepository;
import com.study.springboot.service.QnaCommentService;
import com.study.springboot.service.QnaService;
import com.study.springboot.service.Service2;
import com.study.springboot.service.Service4;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
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
public String content(@PathVariable("id") long id,
                      Model model) {
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

// Qna 시작

// 게시판에서 문의작성눌렀을떄 글쓰는 폼 들어가기
    @GetMapping("qna/writeForm")
    public String userQnaWrite(){
        return "/user/popup/QnA-write";
    }

// Qna 검색액션받기랑 게시판가기

@GetMapping("qna")
public String qnaSearchAction(@RequestParam(value ="keyword", required = false) String keyword,
                              Model model){
    List<QnaResponseDto> list;
        if(keyword ==null){// 검색기능 없을 때
            list = service4.findEvery();
            //마스킹처리
            List<String> nameList = new ArrayList<>();
            for(int i=0 ; i < list.size();i++){

                String qnaName = list.get(i).getQnaName();
                String qnaHiddenName;
                if (qnaName.length() == 2){
                    qnaHiddenName = qnaName.replace(qnaName.charAt(1), '*');
                }else if(qnaName.length() == 1){
                    qnaHiddenName = qnaName;
                }
                else{
                    qnaHiddenName = qnaName.substring(0,2);
                    for (int j=0; j<qnaName.length()-2; j++){
                        qnaHiddenName += "*";
                    }
                }
                nameList.add(qnaHiddenName);
            }
            // 마스킹 처리 끝
            // 답변불러오기
            List<Long> qnaCommentCount = new ArrayList<>();
            for(int i =0; i< list.size(); i++){
                Long CommentCount = service4.countByQnaId(list.get(i).getQnaId());
                qnaCommentCount.add(CommentCount);
            }
            model.addAttribute("qnaCommentCount",qnaCommentCount);
            model.addAttribute("list",list);
            model.addAttribute("namelist",nameList);

            return "/user/category/QnA";
        }else{ //검색기능 있을 때
             list = service4.keyword(keyword);

            List<String> nameList = new ArrayList<>();
            for(int i=0 ; i < list.size();i++){

                String qnaName = list.get(i).getQnaName();
                String qnaHiddenName;
                if (qnaName.length() == 2){
                    qnaHiddenName = qnaName.replace(qnaName.charAt(1), '*');
                }else if(qnaName.length() == 1){
                    qnaHiddenName = qnaName;
                }
                else{
                    qnaHiddenName = qnaName.substring(0,2);
                    for (int j=0; j<qnaName.length()-2; j++){
                        qnaHiddenName += "*";
                    }
                }
                nameList.add(qnaHiddenName);
            }

            List<Long> qnaCommentCount = new ArrayList<>();
            for(int i =0; i< list.size(); i++){
                Long CommentCount = service4.countByQnaId(list.get(i).getQnaId());
                qnaCommentCount.add(CommentCount);
            }
            model.addAttribute("namelist",nameList);
            model.addAttribute("list",list);
            model.addAttribute("qnaCommentCount",qnaCommentCount);
            return "/user/category/QnA";
        }
    }

    @PostMapping("user/qna/write")
    @ResponseBody
    public String userQnaWriteAction(QnaSaveDto saveDto){

        if( saveDto.getQnaSecret() == null ){
            saveDto.setQnaSecret("공개");
        }

        QnaEntity qnaEntity= saveDto.toEntity();
        boolean qnaSave = service4.qnaSave(qnaEntity);
        if(!qnaSave){
            return "<script>alert('등록 실패 하였습니다'); history.back();</script>";
        }
        return "<script>alert('등록에 성공 하였습니다.'); location.href='/qna';</script>";
    }
    //리스트로감


    // 글 삭제
    @GetMapping("user/qna/delete/{id}")
    @ResponseBody
    public String userDelete (@PathVariable("id")long id){
        boolean delete = service4.delete(id);
        if(!delete){
            return "<script>alert('삭제 실패 하였습니다'); history.back();</script>";
        }
        return "<script>alert('삭제 성공 하였습니다.'); location.href='/qna';</script>";
    }
    //qna 리스트가기

    //qna 비밀번호 체크
    // 수정버튼눌렀을때
    @PostMapping("pw/check/action")
    @ResponseBody
    public String pwCheckAction(@ModelAttribute QnaSaveDto qnaSaveDto){
        System.out.println("qnaSaveDto = " + qnaSaveDto);
        Long num = qnaSaveDto.getQnaId();
        boolean pwCheckResult = service4.pwCheck(qnaSaveDto);
        if(!pwCheckResult){
            return "<script>alert('비밀번호가 틀렸습니다.'); history.back();</script>";
        }
        return "<script>alert('비밀번호가 일치하였습니다.'); location.href='/qna/modifyForm/"+ num +"';</script>";
    }
    //qna 비밀번호 체크
    // 삭제버튼 눌렀을때
    @PostMapping("pw/check/action2")
    @ResponseBody
    public String pwCheckAction2(@ModelAttribute QnaSaveDto qnaSaveDto){
        System.out.println("qnaSaveDto = " + qnaSaveDto);
        Long num = qnaSaveDto.getQnaId();
        boolean pwCheckResult = service4.pwCheck(qnaSaveDto);
        if(!pwCheckResult){
            return "<script>alert('비밀번호가 틀렸습니다.'); history.back();</script>";
        }
        return "<script>alert('비밀번호가 일치하였습니다.'); location.href='/user/qna/delete/"+ num +"';</script>";
    }

    //수정페이지
    @GetMapping("qna/modifyForm/{num}")
    public String modifyForm(@PathVariable("num")Long num,
                             Model model){
        QnaResponseDto qnaResponseDto = service4.findById(num);
        model.addAttribute("dto",qnaResponseDto);
        return "/user/popup/QnA-modify";
    }

    //수정 액션받기
    @PostMapping("qna/modifyForm")
    @ResponseBody
    public String qnaModify(@ModelAttribute QnaSaveDto dto){
        if( dto.getQnaSecret() == null ){
            dto.setQnaSecret("공개");
        }
        QnaEntity qnaEntity = dto.toModifyEntity();
        boolean modifyResult = service4.qnaSave(qnaEntity);
        if(!modifyResult){
            return "<script>alert('수정 실패했습니다.'); history.back();</script>";
        }
        return "<script>alert('수정 성공하였습니다.'); location.href='/qna';</script>";
    }
}
//qnaList