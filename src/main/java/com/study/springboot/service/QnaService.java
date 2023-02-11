package com.study.springboot.service;

import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.entity.QnaEntity;
import com.study.springboot.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class QnaService {

    private final QnaRepository qnaRepository;


    @Transactional
    public boolean save(QnaEntity qnaEntity){
        try{
            qnaRepository.save(qnaEntity);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public  List<QnaResponseDto> findAll() {
        // 정렬기능 추가
        //Sort sort = Sort.by(Sort.Direction.DESC);
        List<QnaEntity> list = qnaRepository.findAll();
        return list.stream().map(QnaResponseDto::new).collect(Collectors.toList());
    }
    // 특정게시글 삭제
    public boolean QnaDelete(Long id) {
        try {
            qnaRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    //
    public List<QnaResponseDto> findbyIdx(long idx) {
        Optional<QnaEntity> list = qnaRepository.findById(idx);
        return list.stream().map(QnaResponseDto::new).collect(Collectors.toList());
    }

}
