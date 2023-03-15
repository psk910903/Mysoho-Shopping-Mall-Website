package com.study.springboot.controller.admin;

import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.member.MemberSaveRequestDto;
import com.study.springboot.entity.repository.MemberRepository;
import com.study.springboot.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/admin/member")
    public String adminMember() {
        return "redirect:/admin/member/list?page=0";
    }

    //관리자 회원 목록
    @GetMapping("/admin/member/list")
    public String adminMemberList(@RequestParam(value = "findByType1", required = false) String findByType1,
                                  @RequestParam(value = "findByType2", required = false) String findByType2,
                                  @RequestParam(value = "keyword", required = false) String keyword, // keyword: 어떤 keyword로 찾을 것인지 결정
                                  @RequestParam(value = "page", defaultValue = "0") int page,        // page: 0에서부터 시작
                                  Model model) {                                                    // ex) findBy=title, keyword="키워드입니다", page:2면

        Page<MemberResponseDto> list;
        int totalPage;
        List<Integer> pageList;

        if ((findByType1 == null) && (findByType2 == null) && (keyword == null)
                || (findByType1.equals("null")) && (findByType2.equals("null")) && (keyword.equals("null"))
                || (findByType1.equals("all")) && (findByType2.equals("all")) && (keyword.equals(""))) {

            list = memberService.findAll(page);

        } else {
            list = memberService.findByKeyword(findByType1, findByType2, keyword, page);
        }
        totalPage = list.getTotalPages();
        pageList = memberService.getPageList(totalPage, page);
        model.addAttribute("list", list);
        model.addAttribute("findByType1", findByType1);
        model.addAttribute("findByType2", findByType2);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageList", pageList);
        model.addAttribute("totalPage", totalPage);

        //검색 회원 명수
        long listCount = memberRepository.count();
        model.addAttribute("listCount", listCount);

        return "/admin/member/list"; //listForm.html로 응답
    }

    //단건조회
    @GetMapping("/admin/member/content/{id}")
    public String adminMemberContent(@PathVariable("id") long id, Model model) {
        MemberResponseDto dto = memberService.findById(id);
        model.addAttribute("member", dto);

        return "/admin/member/content";
    }

    //수정
    @PostMapping("/admin/member/content/modify")
    @ResponseBody
    public String adminMemberContentModify(MemberSaveRequestDto dto) {


        boolean result = memberService.modify(dto);

        if (result) {
            return "<script>alert('회원정보수정 완료'); location.href='/admin/member/list';</script>";
        } else {
            return "<script>alert('회원정보수정 실패'); history.back();</script>";
        }
    }

    @RequestMapping("/admin/member/delete/check")
    public String adminMemberDeleteCheck(@RequestParam("memberNo") String memberNo) {
        memberService.deleteCheck(memberNo);
        return "redirect:/admin/member/list";
    }
}
