package com.study.springboot.service;

import com.study.springboot.dto.qna.AdminReplyResponseDto;
import com.study.springboot.entity.qna.AdminReplyEntity;
import com.study.springboot.repository.AdminReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminReplyService {
    private final AdminReplyRepository adminReplyRepository;

    @Transactional
    public boolean save(AdminReplyEntity adminReplyEntity){
        try{
            adminReplyRepository.save(adminReplyEntity);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //삭제기능
    public boolean AdminReplyDelete (Long id) {
        try {
            adminReplyRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public List<AdminReplyResponseDto> findbyidx(long idx) {
        Optional<AdminReplyEntity> list = adminReplyRepository.findById(idx);
        return list.stream().map(AdminReplyResponseDto::new).collect(Collectors.toList());
    }
}
