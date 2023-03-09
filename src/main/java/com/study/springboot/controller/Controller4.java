package com.study.springboot.controller;
import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.qna.QnaCommentResponseDto;
import com.study.springboot.dto.qna.QnaCommentSaveDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.dto.qna.QnaSaveDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.entity.QnaEntity;
import com.study.springboot.repository.QnaRepository;
import com.study.springboot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Controller
@RequestMapping("/")
//   /admin/qna

public class Controller4 {
    final private PasswordEncoder passwordEncoder;
    private final Service4 service4;
    private final Service3 service3;

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
        model.addAttribute("dateStart", dateStart);
        model.addAttribute("dateEnd", dateEnd);
        model.addAttribute("qnalist", list);
        model.addAttribute("pageList", pageList);

        long listCount = qnaRepository.count();
        model.addAttribute("listCount", listCount);
        return "/admin/qna/list";

    }

    @GetMapping("admin/qna/content/{id}")
    public String content(@PathVariable("id") long id,
                          Model model) {

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


    // 게시판에서 문의작성눌렀을떄 글쓰는 폼 들어가기
    // 로그인 Q&A 페이지 가기
    @GetMapping("qna/writeForm")
    public String userQnaWrite( @AuthenticationPrincipal User user,
                                @RequestParam String reference,
                                Model model){

        String username = user.getUsername();
        MemberEntity entity = service3.findByUserId(username);

        model.addAttribute("reference", reference);
        model.addAttribute("userName",entity.getUsername());
        model.addAttribute("userPassword",entity.getPassword());

        return "/user/popup/qna-write";

    }

    // 비로그인 Q&A 페이지가기
    @GetMapping("qna/writeFormGuest")
    public String userQnaWriteGuest(@RequestParam String reference,
                                    Model model){
        model.addAttribute("reference", reference);
        return "/user/popup/qna-write";
    }

    // Qna 검색액션받기랑 게시판가기
    @GetMapping("qna")
    public String qnaSearchAction(@RequestParam(value ="keyword", required = false) String keyword,
                                  HttpServletRequest request,
                                  Model model, @AuthenticationPrincipal User user){

        // memberId 보내기
        String memberId = null;
        if (user != null){
            memberId = user.getUsername();
        }
        model.addAttribute("memberId", memberId);

        List<QnaResponseDto> list;
        if(keyword ==null){
            // 검색기능 없을 때

            list = service4.findEvery();
            //마스킹처리
            List<String> nameList = new ArrayList<>();
            for(int i=0 ; i < list.size();i++){

                String qnaName = list.get(i).getQnaName();
                if(qnaName == null){
                    qnaName = list.get(i).getMemberId();
                }
                String qnaHiddenName;
                if (qnaName.length() == 2){
                    qnaHiddenName = qnaName.replace(qnaName.charAt(1), '*');
                }else if(qnaName.length() == 1){
                    qnaHiddenName = qnaName;
                }
                else{
                    qnaHiddenName = qnaName.substring(0,2);;
                    //
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
            //답변카운트 불러오기 끝
            model.addAttribute("keyword",keyword);
            model.addAttribute("listCount", list.size());
            model.addAttribute("qnaCommentCount",qnaCommentCount);
            model.addAttribute("list",list);
            model.addAttribute("namelist",nameList);

            return "/user/category/qna";

        }else{ //검색기능 있을 때

            list = service4.keyword(keyword);

            //마스킹처리
            List<String> nameList = new ArrayList<>();
            for(int i=0 ; i < list.size();i++){

                String qnaName = list.get(i).getQnaName();
                if(qnaName == null){
                    qnaName = list.get(i).getMemberId();
                }
                String qnaHiddenName;
                if (qnaName.length() == 2){
                    qnaHiddenName = qnaName.replace(qnaName.charAt(1), '*');
                }else if(qnaName.length() == 1){
                    qnaHiddenName = qnaName;
                }
                else{
                    qnaHiddenName = qnaName.substring(0,2);;
                    //
                    for (int j=0; j<qnaName.length()-2; j++){
                        qnaHiddenName += "*";
                    }
                }
                nameList.add(qnaHiddenName);
            }
            //마스킹처리끝

            //답변카운트 불러오기
            List<Long> qnaCommentCount = new ArrayList<>();
            for(int i =0; i< list.size(); i++){
                Long CommentCount = service4.countByQnaId(list.get(i).getQnaId());
                qnaCommentCount.add(CommentCount);
            }
            //답변카운트 불러오기 끝
            model.addAttribute("keyword",keyword);
            model.addAttribute("listCount", list.size());
            model.addAttribute("namelist",nameList);
            model.addAttribute("list",list);
            model.addAttribute("qnaCommentCount",qnaCommentCount);
            return "/user/category/qna";
        }
    }


    @PostMapping("qna/write")
    @ResponseBody
    public String userQnaWriteAction(QnaSaveDto saveDto, @RequestParam String reference){

        if( saveDto.getQnaSecret() == null ){
            saveDto.setQnaSecret("공개");
        }
        if( saveDto.getMemberId() != null ){
            MemberEntity entity = service3.findByUserId(saveDto.getMemberId());
            saveDto.setQnaName(entity.getMemberName());
        }

        QnaEntity qnaEntity= saveDto.toEntity();
        boolean qnaSave = service4.qnaSave(qnaEntity);
        if(!qnaSave){
            return "<script>alert('등록 실패 하였습니다'); history.back();</script>";
        }
        // return "<script>alert('등록되었습니다');opener.parent.location.reload();window.close();</script>";
        return "<script>alert('등록되었습니다'); location.href='" + reference + "';</script>";
    }
    //리스트로감

    // 글 삭제

    @GetMapping("qna/delete/{id}")
    @ResponseBody
    public String userDelete (@PathVariable("id")long id){

        boolean delete = service4.delete(id);
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
        boolean pwCheckResult = service4.pwCheck(qnaSaveDto);
        if(!pwCheckResult){

            return "<script>alert('비밀번호 확인실패'); history.back();</script>";
        }

        /*
        return "<script>" +
                "alert('비밀번호 확인완료\\n창이 뜨지 않을 경우 팝업 차단 해제를 해주세요.');" +
                "window.open('/qna/modifyForm/" + num + "');" +
                "location.href='/qna'" +
                "</script>";

         */

        return "<script>" +
                "alert('비밀번호 확인완료');" +
                "location.href='/qna/modifyForm/" + num + "?reference=/qna'"+
                "</script>";

    }
    @PostMapping("qna/pw/check/action2/guest")
    @ResponseBody
    public String pwCheckAction2(@ModelAttribute QnaSaveDto qnaSaveDto){

        Long num = qnaSaveDto.getQnaId();
        boolean pwCheckResult = service4.pwCheck(qnaSaveDto);
        if(!pwCheckResult){
            return "<script>alert('비밀번호 확인실패'); history.back();</script>";
        }

        return "<script>location.href='/qna/delete/"+ num +"';</script>";

    }

    //수정페이지

    @GetMapping("qna/modifyForm/{num}")
    public String modifyForm(@PathVariable("num")Long num,
                             @RequestParam String reference,
                             Model model){

        QnaResponseDto qnaResponseDto = service4.findById(num);

        List <QnaCommentResponseDto> commentList = service4.findAllByCommentQnaId(num);

        model.addAttribute("reference", reference);
        model.addAttribute("commentList", commentList);
        model.addAttribute("dto",qnaResponseDto);

        return "/user/popup/qna-modify";
    }

    //수정 액션받기
    @PostMapping("qna/modify")
    @ResponseBody
    public String qnaModify(@ModelAttribute QnaSaveDto dto, @RequestParam String reference){

        if( dto.getQnaSecret() == null ){
            dto.setQnaSecret("공개");
        }
        if( dto.getMemberId() != null ){
            MemberEntity entity = service3.findByUserId(dto.getMemberId());
            dto.setQnaName(entity.getMemberName());
        }
        QnaEntity qnaEntity = dto.toModifyEntity();
        boolean modifyResult = service4.qnaSave(qnaEntity);
        if(!modifyResult){
            return "<script>alert('수정 실패했습니다.'); history.back();</script>";
        }
        // return "<script>alert('수정 되었습니다');opener.parent.location.reload();window.close();</script>";
        return "<script>alert('수정 되었습니다'); location.href='" + reference + "';</script>";
    }

}
//qnaList