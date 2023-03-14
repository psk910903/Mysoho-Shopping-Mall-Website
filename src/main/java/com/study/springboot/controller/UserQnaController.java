package com.study.springboot.controller;

import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.repository.NoticeRepository;
import com.study.springboot.service.AwsS3Service;
import com.study.springboot.service.NoticeService;
import com.study.springboot.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserQnaController {
    private final NoticeService noticeService;
    private final AwsS3Service awsS3Service;
    private final NoticeRepository noticeRepository;
    private final QnaService qnaService;

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
            // replyCountList
            Long replyCount = qnaService.countByQnaId(qnaDto.getQnaId());
            replyCountList.add(replyCount);
        }

        // memberHiddenName
        String memberName = memberId; // "홍길동임"
        String memberHiddenName; // "홍길**"

        if (memberName.length() <= 2){
            memberHiddenName = memberName;
        }
        else{
            memberHiddenName = memberName.substring(0,2);
            for (int i=0; i<memberName.length()-2; i++) memberHiddenName += "*";
        }

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
}
