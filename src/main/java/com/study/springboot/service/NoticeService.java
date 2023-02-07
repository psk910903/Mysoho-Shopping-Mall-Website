package com.study.springboot.service;

import com.study.springboot.dto.notice.NoticeResponseDto;
import com.study.springboot.entity.NoticeEntity;
import com.study.springboot.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public List<NoticeResponseDto> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "noticeNo");
        List<NoticeEntity> list = noticeRepository.findAll(sort);
        return list.stream().map(NoticeResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NoticeResponseDto findById(Long noticeNo) {
        Optional<NoticeEntity> entity = noticeRepository.findById(noticeNo);
//                .orElseThrow( () -> new IllegalArgumentException("해당 사용자가 없습니다. board_idx="+board_idx) );
        if (entity.isPresent()){
            return 
        }

        return new NoticeResponseDto(entity);
    }

}
