package com.study.springboot.controller.User;

//import com.study.springboot.CaptchaUtil;
import com.study.springboot.CaptchaUtil;
import com.study.springboot.dto.qna.QnaCommentResponseDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.dto.qna.QnaSaveDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.entity.QnaEntity;
import com.study.springboot.service.*;
import lombok.RequiredArgsConstructor;
import nl.captcha.Captcha;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserQnaController {

    private final QnaService qnaService;
    private final QnaCommentService qnaCommentService;
    private final MemberService memberService;
    private final InquiryService inquiryService;

    //qna 작성 팝업 폼
    @GetMapping("/popup/qna-write")
    public String popupQnaWrite() {
        return "/user/popup/qna-write";
    }

    //마이페이지 qna
    @GetMapping("/qna/user")
    public String qnaUser(Model model,
                          @AuthenticationPrincipal User user) {
        String memberId = user.getUsername();
        List<QnaResponseDto> qnaList = qnaService.findByMemberIdQna(memberId);
        List<Long> replyCountList = new ArrayList<>();

        for(QnaResponseDto qnaDto : qnaList) {
            Long replyCount = qnaService.countByQnaId(qnaDto.getQnaId());
            replyCountList.add(replyCount);
        }
        String memberHiddenName = inquiryService.maskingId(memberId);

        model.addAttribute("qnaList", qnaList);
        model.addAttribute("replyCountList", replyCountList);
        model.addAttribute("memberHiddenName", memberHiddenName);

        return "user/user/qna-user";
    }

    @PostMapping("/qna/user/deleteAction")
    @ResponseBody
    public String qnaUserDeleteAction(@RequestParam Long qnaId) {

        Boolean success = qnaService.delete(qnaId);
        if(success) {
            return "<script>alert('삭제되었습니다.'); location.href='/qna/user';</script>";
        }else{
            return "<script>alert('삭제 실패했습니다.'); history.back();</script>";
        }
    }

    @GetMapping("/qna/writeForm")
    public String userQnaWrite( @AuthenticationPrincipal User user,
                                @RequestParam String reference,
                                Model model){

        String username = user.getUsername();
        MemberEntity entity = memberService.findByUserId(username);

        model.addAttribute("reference", reference);
        model.addAttribute("userName",entity.getUsername());
        model.addAttribute("userPassword",entity.getPassword());

        return "/user/popup/qna-write";

    }

    @GetMapping("/qna/writeFormGuest")
    public String userQnaWriteGuest(@RequestParam String reference,
                                    Model model){
        model.addAttribute("reference", reference);
        return "/user/popup/qna-write";
    }

    @GetMapping("/qna")
    public String qnaSearchAction(@RequestParam(value ="keyword", required = false) String keyword,
                                  HttpServletRequest request,
                                  Model model, @AuthenticationPrincipal User user){

        String memberId = null;
        if (user != null){
            memberId = user.getUsername();
        }
        model.addAttribute("memberId", memberId);

        List<QnaResponseDto> list;
        if(keyword ==null){ // 검색기능 없을 때

            list = qnaService.findAll();
            List<String> nameList = qnaService.qnaMaskingId(list);//마스킹처리
            List<Long> qnaCommentCount = qnaCommentService.qnaCommentCount(list);// 답변불러오기

            model.addAttribute("keyword",keyword);
            model.addAttribute("listCount", list.size());
            model.addAttribute("qnaCommentCount",qnaCommentCount);
            model.addAttribute("list",list);
            model.addAttribute("namelist",nameList);

            return "/user/category/qna";

        }else{ //검색기능 있을 때

            list = qnaService.findByKeyword(keyword);
            List<String> nameList = qnaService.qnaMaskingId(list);//마스킹처리
            List<Long> qnaCommentCount = qnaCommentService.qnaCommentCount(list);// 답변불러오기

            model.addAttribute("keyword",keyword);
            model.addAttribute("listCount", list.size());
            model.addAttribute("namelist",nameList);
            model.addAttribute("list",list);
            model.addAttribute("qnaCommentCount",qnaCommentCount);
            return "/user/category/qna";
        }
    }

    @PostMapping("/qna/write")
    @ResponseBody
    public String userQnaWriteAction(HttpServletRequest req, QnaSaveDto saveDto, @RequestParam String reference, @RequestParam String captchaText){

        if( saveDto.getQnaSecret() == null ){
            saveDto.setQnaSecret("공개");
        }
        if( saveDto.getMemberId() != null ){
            MemberEntity entity = memberService.findByUserId(saveDto.getMemberId());
            saveDto.setQnaName(entity.getMemberName());
        }

        QnaEntity qnaEntity= saveDto.toEntity();
        boolean result = qnaService.qnaSave(qnaEntity);
        Captcha captcha = (Captcha) req.getSession().getAttribute(Captcha.NAME);
        String ans = captchaText;
        if(ans!=null && !"".equals(ans)) {
            if(captcha.isCorrect(ans)) {
                if(!result){
                    return "<script>alert('등록에 실패했습니다.');history.back();</script>";
                }
                return "<script>alert('등록되었습니다');location.href='"+ reference +"';</script>";
            }else {
                return "<script>alert('보안문자를 다시 입력해주세요.');history.back(); </script>";
            }
        }else{
            return "<script>alert('보안문자를 입력해주세요');history.back();</script>";
        }
    }


    // 삭제
    @GetMapping("/qna/delete/{id}")
    @ResponseBody
    public String userDelete (@PathVariable("id")long id){

        boolean delete = qnaService.delete(id);
        if(!delete){
            return "<script>alert('삭제 실패 하였습니다'); history.back();</script>";
        }
        return "<script>alert('삭제 성공하였습니다.'); location.href='/qna';</script>";
    }
    //qna 리스트가기


    //qna 비밀번호 체크
    @PostMapping("/qna/pw/check/action/guest")
    @ResponseBody
    public String pwCheckAction(@ModelAttribute QnaSaveDto qnaSaveDto){

        Long num = qnaSaveDto.getQnaId();
        String url = "/qna/modifyForm/"+num;
        boolean pwCheckResult = qnaService.pwCheck(qnaSaveDto);
        if(!pwCheckResult){

            return "<script>alert('비밀번호 확인실패'); history.back();</script>";
        }

        return "<script>" +
                "alert('비밀번호 확인완료');" +
                "location.href='/qna/modifyForm/" + num + "?reference=/qna'"+
                "</script>";

    }
    @PostMapping("/qna/pw/check/action2/guest")
    @ResponseBody
    public String pwCheckAction2(@ModelAttribute QnaSaveDto qnaSaveDto){

        Long num = qnaSaveDto.getQnaId();
        boolean pwCheckResult = qnaService.pwCheck(qnaSaveDto);
        if(!pwCheckResult){
            return "<script>alert('비밀번호 확인실패'); history.back();</script>";
        }
        return "<script>location.href='/qna/delete/"+ num +"';</script>";
    }

    //수정페이지

    @GetMapping("/qna/modifyForm/{num}")
    public String modifyForm(@PathVariable("num")Long num,
                             @RequestParam String reference,
                             Model model){

        QnaResponseDto qnaResponseDto = qnaService.findById(num);
        List <QnaCommentResponseDto> commentList = qnaCommentService.findAllByCommentQnaId(num);

        model.addAttribute("reference", reference);
        model.addAttribute("commentList", commentList);
        model.addAttribute("dto",qnaResponseDto);

        return "/user/popup/qna-modify";
    }

    //수정 액션받기
    @PostMapping("/qna/modify")
    @ResponseBody
    public String qnaModify(HttpServletRequest req, @ModelAttribute QnaSaveDto dto, @RequestParam String reference, @RequestParam String captchaText){

        if( dto.getQnaSecret() == null ){
            dto.setQnaSecret("공개");
        }
        if( dto.getMemberId() != null ){
            MemberEntity entity = memberService.findByUserId(dto.getMemberId());
            dto.setQnaName(entity.getMemberName());
        }
        QnaEntity qnaEntity = dto.toModifyEntity();
        boolean modifyResult = qnaService.qnaSave(qnaEntity);
        Captcha captcha = (Captcha) req.getSession().getAttribute(Captcha.NAME);
        String ans = captchaText;
        if(ans!=null && !"".equals(ans)) {
            if(captcha.isCorrect(ans)) {
                if(!modifyResult){
                    return "<script>alert('수정 실패했습니다.');history.back();</script>";
                }
                return "<script>alert('수정되었습니다');location.href='"+ reference +"';</script>";
            }else {
                return "<script>alert('보안문자를 다시 입력해주세요.');history.back(); </script>";
            }
        }else{
            return "<script>alert('보안문자를 입력해주세요');history.back();</script>";
        }
    }

}
