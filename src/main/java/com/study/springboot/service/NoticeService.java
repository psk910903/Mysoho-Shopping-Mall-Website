package com.study.springboot.service;

import com.study.springboot.dto.notice.NoticeResponseDto;
import com.study.springboot.dto.notice.NoticeSaveRequestDto;
import com.study.springboot.dto.notice.NoticeUpdateRequestDto;
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
        if (!entity.isPresent()){
            return null;
        }

        return new NoticeResponseDto(entity.get());
    }

    @Transactional
    public Boolean update(final NoticeUpdateRequestDto dto) {

        Optional<NoticeEntity> entity = noticeRepository.findById(dto.getNoticeNo());
        if (!entity.isPresent()){
            return false;
        }

        entity.get().update(dto.getNoticeType(), dto.getNoticeTitle(), dto.getNoticeContent(), dto.getNoticeImageUrl());
        return true;
    }

    @Transactional
    public Boolean save(final NoticeSaveRequestDto dto) {

        try{
            NoticeEntity entity = dto.toEntity();
            noticeRepository.save(entity);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
       return true;
    }

    @Transactional
    public Boolean delete(final Long noticeNo) {
        Optional<NoticeEntity> entity = noticeRepository.findById(noticeNo);
        if (!entity.isPresent()){
            return false;
        }
        try{
            noticeRepository.delete(entity.get());
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
