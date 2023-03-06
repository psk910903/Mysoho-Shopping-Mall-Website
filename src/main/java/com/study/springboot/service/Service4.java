package com.study.springboot.service;

import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.qna.QnaCommentResponseDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.dto.qna.QnaSaveDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.entity.QnaCommentEntity;
import com.study.springboot.entity.QnaEntity;
import com.study.springboot.repository.MemberRepository;
import com.study.springboot.repository.QnaCommentRepository;
import com.study.springboot.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Service4 {
    private final MemberRepository memberRepository;

    private final QnaRepository qnaRepository;
    private final QnaCommentRepository qnaCommentRepository;

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
    public boolean qnaSave(QnaEntity qnaEntity) {
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

    @Transactional
    public boolean pwCheck(QnaSaveDto qnaSaveDto) {
        // 글번호로 DB에서 조회를함
        Optional<QnaEntity> byId = qnaRepository.findById(qnaSaveDto.getQnaId());
        if(byId.isPresent()){// 글번호가 있다.
            QnaEntity qnaEntity = byId.get();
            if(qnaEntity.getQnaPassword().equals(qnaSaveDto.getQnaPassword())){//패스워드 일치
                return true;
            }else{// 패스워드 불일치
                return false;
            }
        }else{// 글번호가 없다.
            return false;
        }
        // DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
    }

    @Transactional
    public QnaResponseDto findById(Long num) {
        Optional<QnaEntity> qnaEntity = qnaRepository.findById(num);
        return new QnaResponseDto(qnaEntity.get());
    }
    @Transactional(readOnly = true)
    public Long countByQnaId(Long qnaId) {
        Long count = qnaCommentRepository.countByQnaId(qnaId);
        return count;
    }

    public List<QnaCommentResponseDto> findAllByCommentQnaId(Long num) {
        List <QnaCommentEntity> qnaCommentEntities = qnaCommentRepository.findAllByCommentQnaId(num);
        return qnaCommentEntities.stream().map(QnaCommentResponseDto::new).collect(Collectors.toList());
    }
}
