package com.study.springboot.service;

import com.study.springboot.dto.security.MemberJoinDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class Service3 {
    final private MemberRepository memberRepository;
    @Transactional(readOnly = true)
    public MemberEntity findByUserId(final String memberId){
        Optional<MemberEntity> optional = memberRepository.findByUserId(memberId);
        return optional.get();
    }

    @Transactional
    public boolean delete(final String username) throws Exception {
        Optional<MemberEntity> optional = memberRepository.findByUserId(username);
        if( !optional.isPresent() ) {
            throw new Exception("member id is not present!");
        }
        MemberEntity entity = optional.get();
        try {
            memberRepository.delete( entity );
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(final MemberJoinDto dto){
        try {
            MemberEntity enity = dto.toUpdateEntity();
            memberRepository.save( enity );
            System.out.println(enity.getMemberName());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}