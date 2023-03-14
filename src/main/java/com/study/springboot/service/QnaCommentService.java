package com.study.springboot.service;

import com.study.springboot.dto.qna.QnaCommentResponseDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.entity.QnaCommentEntity;
import com.study.springboot.entity.repository.QnaCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaCommentService {

    private final QnaCommentRepository qnaCommentRepository;

    public List<QnaCommentResponseDto> findbyIdx(Long idx) {
        List<QnaCommentEntity> commentEntity = qnaCommentRepository.findByCommentQnaId_nativeQuery(idx);
        return commentEntity.stream().map((QnaCommentEntity entity) -> new QnaCommentResponseDto(entity)).collect(Collectors.toList());
    }

    public boolean save(QnaCommentEntity qnaCommentEntity) {
        try{
            qnaCommentRepository.save(qnaCommentEntity);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(Long id){
        try {
            qnaCommentRepository.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<QnaCommentResponseDto> findbyid(long id) {
        Optional<QnaCommentEntity> list = qnaCommentRepository.findById(id);
        return list.stream().map(QnaCommentResponseDto::new).collect(Collectors.toList());
    }

    public boolean modify(QnaCommentEntity entity) {
        try{
            qnaCommentRepository.save(entity);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Long> qnaCommentCount(List<QnaResponseDto> list){
        List<Long> qnaCommentCount = new ArrayList<>();
        for(int i =0; i< list.size(); i++){
            Long CommentCount = countByQnaId(list.get(i).getQnaId());
            qnaCommentCount.add(CommentCount);
        }
        return qnaCommentCount;
    }

    @Transactional(readOnly = true)
    public Long countByQnaId(Long qnaId) {
        Long count = qnaCommentRepository.countByQnaId(qnaId);
        return count;
    }

    public List<QnaCommentResponseDto> findAllByCommentQnaId(Long num) {
        return qnaCommentRepository.findAllByCommentQnaId(num).stream().map(QnaCommentResponseDto::new).collect(Collectors.toList());
    }
}
