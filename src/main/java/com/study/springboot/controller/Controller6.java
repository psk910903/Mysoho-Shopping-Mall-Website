package com.study.springboot.controller;


import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.repository.MemberRepository;
import com.study.springboot.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/member")
public class Controller6 {
    private final MemberRepository memberRepository;
    private final MemberService memberService;


    @GetMapping("/")
    public String home() {
        return "redirect:/admin/member/list?page=0";
    }
    @GetMapping("/list")
    public String list(@RequestParam(value = "findByType1", required = false) String findByType1,
                       @RequestParam(value = "findByType2", required = false) String findByType2,
                       @RequestParam(value = "keyword", required = false) String keyword, // keyword: 어떤 keyword로 찾을 것인지 결정
                       @RequestParam(value = "page", defaultValue = "0") int page,        // page: 0에서부터 시작
                       Model model) {                                                    // ex) findBy=title, keyword="키워드입니다", page:2면
        //     제목에서 "키워드입니다"가 포함된 글 중 3쪽을 보여줌

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

        return "admin/member/list"; //listForm.html로 응답
    }

    @GetMapping("/content/{id}")
//                            @파람detail?id=
    public String content(@PathVariable("id") long id, Model model) {
        MemberResponseDto dto = memberService.findById(id);
        model.addAttribute("member", dto);

        return "/admin/member/content";
    }

    @PostMapping("/content/modify")  //수정
    @ResponseBody
    public  String contentModify(MemberResponseDto dto) {
        MemberResponseDto entity = memberService.modify(Long.valueOf(dto.getMemberNo()), dto);
        if (entity.getMemberNo() == Long.valueOf(dto.getMemberNo())) {
            return "<script>alert('정보수정됨'); location.href='/admin/member/list';</script>";
        } else {                        //http://localhost:8080/admin/member/list?page=0
            //업데이트 실패함
            return "<script>alert('정보수정 실패함'); history.back();</script>";
        }
    }

    @RequestMapping("/delete/check")
    public String deleteCheck (@RequestParam("memberNo") String memberNo) {
        memberService.deleteCheck(memberNo);
        return "redirect:/admin/member/list";
    }
}

