package com.study.springboot.service;

import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Service4 {
    private final MemberRepository memberRepository;

    @Transactional
    public boolean save(MemberEntity entity) {
        try{
            memberRepository.save(entity);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<MemberEntity> findByUserId(String userId) {
        List<MemberEntity> list= memberRepository.findByUserId(userId);
        return list;
    }
}
