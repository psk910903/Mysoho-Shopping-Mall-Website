package com.study.springboot.controller;

import com.study.springboot.dto.BoardResponseDto;
import com.study.springboot.dto.BoardSaveRequestDto;
import com.study.springboot.dto.ReplyResponseDto;
import com.study.springboot.entity.Board;
import com.study.springboot.service.BoardService;
import com.study.springboot.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
//모든 요청을 URL "/board" 밑으로 받겠다는 뜻
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final ReplyService replyService;

    //호출 URL : http://localhost:8080/board/
    @RequestMapping("/")
    public String main(){
        return "redirect:/board/listForm";
    }

    @RequestMapping("/listForm")
    public String listForm( Model model,
                            @RequestParam(value = "page", defaultValue = "0") int page){
        Page<Board> paging = boardService.getList(page); //페이징처리
        model.addAttribute("paging", paging);

        List<BoardResponseDto> list = new ArrayList<>();
        for (Board entity : paging) {
            list.add(new BoardResponseDto(entity));
        }
        model.addAttribute("list", list);

        return "listForm"; //listForm.html로 응답
    }
    @RequestMapping("/writeForm")
    public String writeForm() {
        return "writeForm";//writeForm.html로 응답
    }
    //Input Value를 가져오는 방법
    //1. @RequestParam
    //2. 클래스객체(DTO/VO) 매핑 : Setter 필요함
    //3. 맵(Map) 매핑
    @RequestMapping("/writeAction")
    @ResponseBody
    public String writeAction(BoardSaveRequestDto boardSaveRequestDto) {
        Long new_idx = boardService.save(boardSaveRequestDto);

        boolean isFound = boardService.existsById(new_idx);
        if( isFound == true ) {
            return "<script>alert('글쓰기 성공!'); location.href='/board/listForm';</script>";
        }else{
            return "<script>alert('글쓰기 실패!'); history.back();</script>";
        }
    }
    @RequestMapping("/contentForm")
    public String contentForm(@RequestParam("board_idx") String board_idx,
                              Model model) {
        //게시글 정보 조회
        BoardResponseDto dto = boardService.findById(Long.valueOf(board_idx));
        model.addAttribute("dto", dto);

        //조회수 증가
        BoardSaveRequestDto saveDto = BoardSaveRequestDto.builder()
                                    .board_content(dto.getBoard_content())
                                    .board_title(dto.getBoard_title())
                                    .board_name(dto.getBoard_name())
                                    .board_hit(dto.getBoard_hit() + 1)
                                    .build();
        boardService.update(Long.valueOf(board_idx), saveDto);

        //댓글목록 가져오기
        List<ReplyResponseDto> reply_list = replyService.findAllByReplyBoardIdx(Long.valueOf(board_idx));
        model.addAttribute("reply_list", reply_list);

        return "contentForm";//contentForm.html로 응답
    }
    @RequestMapping("updateAction")
    @ResponseBody
    public String updateAction(BoardSaveRequestDto dto,
                               @RequestParam("board_idx") String board_idx) {

        Board entity = boardService.update(Long.valueOf(board_idx), dto);
        if( entity.getBoardIdx() == Long.valueOf(board_idx) ) {
            //업데이트 성공
            //return "<script>alert('글수정 성공!'); location.href='/board/listForm';</script>";
            return "<script>alert('글수정 성공!'); location.href='/board/contentForm?board_idx=" + board_idx + "';</script>";
        }else{
            //업데이트 실패
            return "<script>alert('글수정 실패!'); history.back();</script>";
        }
    }
    @RequestMapping("/deleteAction")
    @ResponseBody
    public String deleteAction(@RequestParam("board_idx") String board_idx) {

        boardService.delete( Long.valueOf(board_idx) );

        return "<script>alert('글삭제 성공!'); location.href='/board/listForm';</script>";
    }

    @GetMapping("/apiForm")
    public String apiForm(){
        return "apiForm";//apiForm.html로 응답
    }
}
