package com.study.springboot.service;

import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.member.MemberSaveRequestDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Service6 {
    private final MemberRepository memberRepository;


    public boolean pwCheck(MemberSaveRequestDto dto){

        String memberPw = dto.getMemberPw();
        MemberResponseDto responseDto = new MemberResponseDto(memberRepository.findById(dto.getMemberNo()).get());

        if (memberPw.equals(responseDto.getMemberPw())) {
            dto.setMemberPw(dto.getMemberNewPw());
            dto.setMemberJoinDatetime(responseDto.getMemberJoinDatetime());
        } else {
            return false;
        }
        return true;
    }





}
