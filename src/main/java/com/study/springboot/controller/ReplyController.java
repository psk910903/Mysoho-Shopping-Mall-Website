package com.study.springboot.controller;

import com.study.springboot.dto.ReplySaveRequestDto;
import com.study.springboot.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {
    final private ReplyService replyService;

    @RequestMapping("/writeReplyAction")
    @ResponseBody
    public String writeReplyAction(ReplySaveRequestDto dto,
                                   @RequestParam("reply_board_idx") String reply_board_idx){

        Long new_idx = replyService.save( dto );

        boolean isFound = replyService.existsById(new_idx);
        if( isFound == true ) {
            return "<script>alert('댓글쓰기 성공!'); location.href='/board/contentForm?board_idx=" + reply_board_idx + "';</script>";
        }else{
            return "<script>alert('댓글쓰기 실패!'); history.back();</script>";
        }
    }

    @RequestMapping("/deleteReplyAction")
    @ResponseBody
    public String deleteReplyAction(@RequestParam("reply_idx") String reply_idx,
                                    @RequestParam("board_idx") String board_idx){

        replyService.delete( Long.valueOf(reply_idx) );

        return "<script>alert('댓글삭제 성공!'); location.href='/board/contentForm?board_idx=" + board_idx + "';</script>";
    }
}
