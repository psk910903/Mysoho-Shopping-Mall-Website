package com.study.springboot.service;

import com.study.springboot.dto.qna.QnaCommentResponseDto;
import com.study.springboot.entity.QnaCommentEntity;
import com.study.springboot.repository.QnaCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
