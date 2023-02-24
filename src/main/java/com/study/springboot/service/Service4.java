package com.study.springboot.service;

import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.entity.QnaEntity;
import com.study.springboot.repository.MemberRepository;
import com.study.springboot.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Service4 {
    private final MemberRepository memberRepository;
    private final QnaRepository qnaRepository;

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


    public List<QnaResponseDto> findEvery() {

        Sort sort = Sort.by(Sort.Direction.DESC, "qnaId");
        List<QnaEntity> list =qnaRepository.findAll(sort);
        return list.stream().map(QnaResponseDto::new).collect(Collectors.toList());
    }
    @Transactional
    public List<QnaResponseDto> keyword(String keyword) {

        List<QnaEntity> list =qnaRepository.findByQnaContentContaining(keyword);
        return list.stream().map(QnaResponseDto::new).collect(Collectors.toList());
    }
    @Transactional
    public boolean qnasave(QnaEntity qnaEntity) {
        try{
            qnaRepository.save(qnaEntity);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Transactional
    public boolean delete(Long id){
        try {
            qnaRepository.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
