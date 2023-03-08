package com.study.springboot.controller;

import com.study.springboot.dto.inquiry.InReplyResponseDto;
import com.study.springboot.dto.inquiry.InReplySaveResponseDto;
import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.dto.inquiry.InquirySaveResponseDto;
import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.product.ProductSearchDto;
import com.study.springboot.dto.qna.QnaCommentResponseDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.dto.qna.QnaSaveDto;
import com.study.springboot.entity.InquiryEntity;
import com.study.springboot.entity.ProductEntity;
import com.study.springboot.entity.InReplyEntity;
import com.study.springboot.repository.InReplyRepository;
import com.study.springboot.repository.InquiryRepository;
import com.study.springboot.repository.ProductRepository;
import com.study.springboot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@Controller
public class Controller5 {
    public final InquiryService inquiryService;
    public final InquiryRepository inquiryRepository;
    public final InReplyService inReplyService;
    private final InReplyRepository inReplyRepository;

    private final ProductService productService; // 02 22
    private final ProductRepository productRepository; //02 22
    private final Service5 service5; // 02 23
    private final MemberService memberService; //0303

    @GetMapping ("/admin/inquiry")
    public String inquiryMain(){
        return "redirect:/admin/inquiry/list";
    }

    @GetMapping("/admin/inquiry/list")
    public String list(Model model,
                       @RequestParam(value = "dateStart", required = false) String dateStart,
                       @RequestParam(value = "dateEnd", required = false) String dateEnd,
                       @RequestParam(value = "keyword", required = false ) String keyword,
                       @RequestParam(value = "findBy", required = false ) String findBy,
                       @RequestParam(value = "page", defaultValue = "0") int page) throws ParseException {
        Page<InquiryResponseDto> list = null;
        if(keyword == null && findBy == null && dateStart == null && dateEnd == null
                || (dateStart.equals("null")) && (dateEnd.equals("null")) && (keyword.equals("null"))
                || (dateStart.equals("")) && (dateEnd.equals("")) && (keyword.equals(""))){
            list = inquiryService.getPage(page);
        }else {
            if(!dateStart.equals("") && dateEnd.equals("")){
                list = inquiryService.findByDate(dateStart, page);
            } else if ((!dateStart.equals("")) && (!dateEnd.equals(""))) {
                list = inquiryService.findByDate(dateStart, dateEnd, page);
            } else {
                list = inquiryService.findByKeyword(findBy, keyword, page);
            }
        }
        int totalPage = list.getTotalPages();
        List<Integer> pageList = inquiryService.getPageList(totalPage, page);


        List<String> itemList = new ArrayList<>();
        for (InquiryResponseDto dto: list){
            Long itemNo = dto.getItemNo();
            ProductResponseDto itemDto = productService.findById(itemNo);
            itemList.add(itemDto.getItemName());
        }
//        model.addAttribute("count", count);
        model.addAttribute("itemList", itemList);
        model.addAttribute("list", list);
        model.addAttribute("findBy", findBy);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageList", pageList);
        model.addAttribute("dateStart", dateStart);
        model.addAttribute("dateEnd", dateEnd);

        long listCount = inquiryRepository.count();
        model.addAttribute("listCount",listCount );

        return "admin/inquiry/list";
    }

    @RequestMapping ("/admin/inquiry/content")
    public String content(@RequestParam("inquiryNo")Long inquiryNo,
                          //@RequestParam("inquiryNo")Long replyInquiryNo,
                          Model model){

        InquiryResponseDto dto = inquiryService.findById(inquiryNo);
        model.addAttribute("dto",dto);
        List<InReplyEntity> list = inReplyRepository.findAllByReplyInquiryNo(inquiryNo);
        model.addAttribute("list",list);


        Long itemNo = dto.getItemNo();
        ProductResponseDto itemDto = productService.findById(itemNo);
        String itemName = itemDto.getItemName();

        model.addAttribute("itemName", itemName);

        if (list.size() == 0) {
            model.addAttribute("nullCheck", "null");
        }

        return "admin/inquiry/content";
    }

//    리플 달기

    @RequestMapping("/admin/inquiry/writeAction")
    @ResponseBody
    public String replyWrite(InReplySaveResponseDto dto,
                             @RequestParam("replyInquiryNo") Long replyInquiryNo ){
        boolean result = inReplyService.save(dto);
        if(result) {
            return "<script>alert('답변등록 완료'); location.href='/admin/inquiry/content?inquiryNo=" + replyInquiryNo + "'; </script>";
        }else{
            return "<script>alert('답변등록 실패'); history.back();</script>";
        }
    }
    @RequestMapping("/admin/inquiry/modifyAction")
    @ResponseBody
    public String modify(InReplySaveResponseDto dto,
                         @RequestParam("replyNo") Long replyNo,
                         @RequestParam("replyInquiryNo") Long replyInquiryNo){
        String replyContent = dto.getReplyContent();
        Long replyInquiryNo1 = dto.getReplyInquiryNo();
        System.out.println("replyInquiryNo1 = " + replyInquiryNo1);
        System.out.println("replyContent = " + replyContent);
        System.out.println("replyNo = " + replyNo);
        System.out.println("replyInquiryNo = " + replyInquiryNo);
        boolean result = inReplyService.modify( dto,replyNo );
        if(result) {
            return "<script>alert('답변수정 완료'); location.href='/admin/inquiry/content?inquiryNo=" + replyInquiryNo + "'; </script>";
        }else{
            return "<script>alert('답변수정 실패'); history.back();</script>";
        }
    }

    @RequestMapping("/admin/inquiry/deleteAction")
    @ResponseBody
    public String delete(@RequestParam("replyNo") Long replyNo,
                         @RequestParam("replyInquiryNo") Long replyInquiryNo){
        boolean result = inReplyService.delete(replyNo);
        if(result){
            return "<script>alert('답변삭제 완료'); location.href='content?inquiryNo=" + replyInquiryNo + "';</script>";
        }else {
            return "<script>alert('답변삭제 실패'); history.back();</script>";
        }
    }
    // 상품 문의 공개, 비공개 및 등록 결과 출력-------------------------------a
    @PostMapping("/inquiry/productInquiryWriteForm/writeAction")
    @ResponseBody
    public String productInquiryWriteFormWriteAction(InquiryResponseDto inquiryResponseDto) {

        //체크박스를 체크안했을 때, 반환되는 null값을 공개로 전환 ↓
        System.out.println(inquiryResponseDto.getInquirySecret());
        if( inquiryResponseDto.getInquirySecret() == null ){

            inquiryResponseDto.setInquirySecret("공개");
        }

        boolean result= service5.save(inquiryResponseDto);
        if(!result){
            return "<script>alert('등록에 실패하였습니다');history.back();</script>";
        }
        return "<script>alert('등록되었습니다');opener.parent.location.reload();window.close();</script>";


    }

    // 상품 문의작성 폼(회원/비회원 나누기)------------------------------↓
    @GetMapping("/inquiry/productInquiryWriteForm/{itemNo}")
    public String inquiryProductInquiryWriteForm(@PathVariable("itemNo") String itemNo, Model model,
                                                 @AuthenticationPrincipal User user) {
        if(user!=null) { // 회원일 때
            String memberId = user.getUsername();
            model.addAttribute("inquiryMemberId", memberId);
        }else {// 비회원일때
            model.addAttribute("inquiryMemberId", null);
        }

        model.addAttribute("itemNo", itemNo);

        return "/user/popup/inquiry-write";
    }

    //상품상세-----------------------------------------------------------------------------↓ 0303 리스트 출력 테스트
    // 댓글 출력 및 아이디 마스킹 contentTest.html과 연동
    @GetMapping("/product/test/{itemNo}")
    public String productContent(Model model, @PathVariable(value = "itemNo") Long itemNo,
                                 HttpServletRequest request){
        ProductResponseDto dto = productService.findById(itemNo);
        String[] colorList = dto.getItemOptionColor().split(",");
        String[] sizeList = dto.getItemOptionSize().split(",");
        int colorCount = colorList.length;
        int sizeCount = sizeList.length;

        List<InquiryResponseDto> list2 = service5.findAll();
        int listSize = list2.size();

        //세션 가져오기
        HttpSession session = request.getSession();
        String name = (String)session.getAttribute("username");
        System.out.println(name);
        //세션 설정하기
        session.setAttribute("name", name);

        //마스킹처리
        List<String> nameList = new ArrayList<>();
        for(int i=0 ; i < list2.size();i++){

            String qnaName = list2.get(i).getInquiryNickname();
            if(qnaName == null){
                qnaName = list2.get(i).getMemberId();
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

        List<Long> inReplyCount = new ArrayList<>();
        for(int i =0; i< list2.size(); i++){
            Long CommentCount = service5.countByInquriyNo(list2.get(i).getInquiryNo());
            inReplyCount.add(CommentCount);
        }
        model.addAttribute("colorCount", colorCount);
        model.addAttribute("sizeCount", sizeCount);
        model.addAttribute("colorList", colorList);
        model.addAttribute("sizeList", sizeList);
        model.addAttribute("dto", dto);
        model.addAttribute("namelist",nameList);
        model.addAttribute("inquiry",list2);
        model.addAttribute("listSize",listSize);
        model.addAttribute("inReplyCount", inReplyCount);
        return "/user/product/contentTest";
    }
    //상품상세-----------------------------------------------------------------------------↑ 0303 리스트 출력 테스트
//로그인일떄 삭제 버튼눌렀을 때
    @GetMapping("/inquiry/delete/{id}")
    @ResponseBody
    public String inquiryDelete(@PathVariable("id")long id){
        long itemNo = service5.findByItemNo(id);
        boolean deleteResult = service5.inquiryDelete(id);
        if(!deleteResult){
            return "<script>alert('삭제 실패');history.back();</script>";
        }
        return "<script>alert('삭제 성공'); location.href='/product/test/"+itemNo+"';</script>";
    }

    //비로그인 삭제 버튼 눌렀을 때
    @PostMapping("/inquiry/pw/check/action2")
    @ResponseBody
    public String pwCheckAction2(@ModelAttribute InquiryResponseDto inquiryResponseDto){

        long inquiryNo = inquiryResponseDto.getInquiryNo();
        boolean pwCheckResult = service5.inquirypwCheck(inquiryResponseDto);
        if(!pwCheckResult){
            return "<script>alert('비밀번호 확인실패'); history.back();</script>";
        }
        return "<script>alert('비밀번호 확인완료.'); location.href='/inquiry/delete/"+inquiryNo+"';</script>";
    }

    //로그인시 수정버튼눌렀을때
    @GetMapping("/inquiry/modifyForm/{num}")
    public String modifyForm(@PathVariable("num")Long num,
                             Model model){

        InquiryResponseDto inquiryResponseDto = service5.findById(num);

        model.addAttribute("dto",inquiryResponseDto);

        return "/user/popup/Inquiry-modify";
    }

    @PostMapping("/inquiry/modify/action")
    @ResponseBody
    public String modifyAction(@ModelAttribute InquiryResponseDto inquiryResponseDto){

        long itemNo = service5.findByItemNo(inquiryResponseDto.getInquiryNo());

        if(inquiryResponseDto.getInquirySecret() == null){
            inquiryResponseDto.setInquirySecret("공개");
        }

        InquiryEntity inquiryEntity = inquiryResponseDto.toModifyEntity();
        boolean modifyResult = service5.inquirySave(inquiryEntity);

        if(!modifyResult){
            return "<script>alert('수정 실패했습니다.');history.back();</script>";
        }
        return "<script>alert('수정되었습니다');location.href='/product/test/"+itemNo+"';</script>";
    }

    @PostMapping("/inquiry/pw/check/action")
    @ResponseBody
    public String pwCheckAction(@ModelAttribute InquiryResponseDto inquiryResponseDto){

        long inquiryNo = inquiryResponseDto.getInquiryNo();
        boolean pwCheckResult = service5.inquirypwCheck(inquiryResponseDto);
        if(!pwCheckResult){
            return "<script>alert('비밀번호 확인실패'); history.back();</script>";
        }
        return "<script>alert('비밀번호 확인완료.'); location.href='/inquiry/modifyForm/"+inquiryNo+"';</script>";
    }
}
