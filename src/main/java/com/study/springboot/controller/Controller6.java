package com.study.springboot.controller;


import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.entity.Member.MemberEntity;
import com.study.springboot.repository.MemberRepository;
import com.study.springboot.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class Controller6 {
    private final MemberRepository memberRepository;
    private final MemberService memberService;


    @GetMapping("/adminHome/memberList")
    public String memberList(Model model){
        List<MemberEntity> list = memberRepository.findAll();

        model.addAttribute("list", list);

        return "/admin/member/memberList";
    }

    @GetMapping("/adminHome/detail/{id}")
//                            @파람detail?id=
    public String detail(@PathVariable("id") long id, Model model) {
        MemberResponseDto dto = memberService.findById(id);
        model.addAttribute("member", dto);

        return "/admin/member/MemberInfo";
    }
 //detail?id=
    @GetMapping("/adminHome/detail/MemberList")
    public String detailMemberList(Model model) {
        List<MemberEntity> list = memberRepository.findAll();

        model.addAttribute("list", list);

        return "/admin/member/memberList";
    }

    @PostMapping("/adminHome/detail/updateMember")
    @ResponseBody
    public  String updateMember( MemberResponseDto dto,
                                 @RequestParam("member_no") String member_no) {
        MemberResponseDto entity = memberService.update(Long.valueOf(member_no), dto);
        if (entity.getMember_no() == Long.valueOf(member_no)) {
            return "<script>alert('정보수정됨'); location.href='/adminHome/detail/"+member_no+"';</script>";
        } else {
            //업데이트 실패
            return "<script>alert('정보수정 실패함'); history.back();</script>";
        }
    }

    @PostMapping("/deleteMember")
    @ResponseBody
    public String deleteMember(@RequestParam("member_no")Long member_no){

        memberService.delete(Long.valueOf(member_no));

        return "<script>alert('회원삭제 성공!'); location.href='/adminHome/memberList';</script>";
    }


}

