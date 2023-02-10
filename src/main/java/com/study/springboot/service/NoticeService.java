package com.study.springboot.service;

import com.study.springboot.dto.notice.NoticeResponseDto;
import com.study.springboot.dto.notice.NoticeSaveRequestDto;
import com.study.springboot.dto.notice.NoticeUpdateRequestDto;
import com.study.springboot.entity.NoticeEntity;
import com.study.springboot.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public Page<NoticeResponseDto> findAll(int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("noticeNo"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<NoticeEntity> list = noticeRepository.findAll(pageable);

        return list.map(NoticeResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<NoticeResponseDto> findByKeyword(String findBy, String keyword, int page) {

        Page<NoticeEntity> list;
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("noticeNo"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        if (findBy.equals("title")){
            list = noticeRepository.findByNoticeTitleContaining(keyword, pageable);
        }else if (findBy.equals("content")){
            list = noticeRepository.findByNoticeContentContaining(keyword, pageable);
        }else{
            list = noticeRepository.findByNoticeType(keyword, pageable);
        }

        return list.map(NoticeResponseDto::new);
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

    public List<Integer> getPageList(final int totalPage, final int page) {

        List<Integer> pageList = new ArrayList<>();

        if (totalPage <= 5){
            for (Integer i=0; i<=totalPage-1; i++){
                pageList.add(i);
            }
        }else if(page >= 0 && page <= 2){
            for (Integer i=0; i<=4; i++){
                pageList.add(i);
            }
        }
        else if (page >= totalPage-3 && page <= totalPage-1){
            for (Integer i=5; i>=1; i--){
                pageList.add(totalPage - i);
            }
        }else{
            for (Integer i=-2; i<=2; i++){
                pageList.add(page + i);
            }
        }

        return pageList;
    }
}
