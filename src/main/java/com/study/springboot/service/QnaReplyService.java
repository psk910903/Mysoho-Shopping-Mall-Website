package com.study.springboot.service;

import com.study.springboot.dto.qna.QnaReplyResponseDto;
import com.study.springboot.dto.qna.QnaReplySaveDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.dto.qna.QnaSaveDto;
import com.study.springboot.entity.QnaReplyEntity;
import com.study.springboot.repository.QnaReplyRepository;
import com.study.springboot.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaReplyService {

    private final QnaReplyRepository qnaReplyRepository;

    @Transactional
    public boolean save(QnaReplyEntity qnaReplyEntity) {
        try{
            qnaReplyRepository.save(qnaReplyEntity);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //삭제기능
    public boolean QnaReplyDelete(Long id) {
        try {
            qnaReplyRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public List<QnaReplyResponseDto> findbyIdx(long idx) {
        Optional<QnaReplyEntity> list = qnaReplyRepository.findById(idx);
        return list.stream().map(QnaReplyResponseDto::new).collect(Collectors.toList());
    }

}
