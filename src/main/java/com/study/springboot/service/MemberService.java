package com.study.springboot.service;

import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.entity.Member.MemberEntity;
import com.study.springboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

//    public void delete(final Long member_no) {
//        MemberEntity memberEntity = memberRepository.findById(member_no)
//                .orElseThrow() -> new IllegalArgumentException()
//    }

//    @GetMapping("/member-delete/{index}")
//    public String member_delete(@PathVariable("index") Integer index,
//                                Model model){
//
//        list.remove( index.intValue() );
//
//        return "redirect:/member/all";
//    }

//    =================================================================
//    @Transactional
//    public MemberEntity
public MemberResponseDto findById(long id) {
    MemberEntity entity = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    MemberResponseDto dto = new MemberResponseDto(entity);
    return dto;
}

@Transactional
public MemberResponseDto update(final Long member_no, final MemberResponseDto memberResponseDto){
    MemberEntity entity = memberRepository.findById(member_no)
            .orElseThrow(() -> new IllegalArgumentException("없는 사용자"));

    entity.update(memberResponseDto.getMember_name(),memberResponseDto.getMember_phone(),
            memberResponseDto.getMember_address(),memberResponseDto.getMember_email(), entity.getMember_no());
    
    System.out.println(entity.getMember_id()); // 서버저장확인
    
    MemberEntity newEntity = memberRepository.save(entity);
    MemberResponseDto dto = new MemberResponseDto(newEntity);
    return dto;
}


    @Transactional
    public void delete(final Long member_no){
        MemberEntity entity = memberRepository.findById(member_no)
                .orElseThrow( ()-> new IllegalArgumentException("해당 회원이 없습니다"));
        memberRepository.delete(entity);
        
    } 


}
