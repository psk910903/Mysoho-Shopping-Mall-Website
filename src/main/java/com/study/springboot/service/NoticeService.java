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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 전체 찾기
    @Transactional(readOnly = true)
    public Page<NoticeResponseDto> findAll(int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("noticeNo"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<NoticeEntity> list = noticeRepository.findAll(pageable);

        return list.map(NoticeResponseDto::new);
    }

    @Transactional(readOnly = true)
    public List<NoticeResponseDto> findAll() {

        List<NoticeEntity> list = noticeRepository.findAll();
        return list.stream().map(NoticeResponseDto::new).collect(Collectors.toList());
    }

    // 키워드로 찾기
    @Transactional(readOnly = true)
    public Page<NoticeResponseDto> findByKeyword(String findBy, String keyword, int page) {

        Page<NoticeEntity> list;
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("noticeNo"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        if (findBy.equals("title")){ // 제목으로 찾을 때
            list = noticeRepository.findByNoticeTitleContaining(keyword, pageable);
        }else if (findBy.equals("content")){ // 내용으로 찾을 때
            pageable = PageRequest.of(page, 10);
            list = noticeRepository.findByNoticeContentContaining(keyword, pageable);
        }else{ // 공지사항인지, 이벤트인지 구분할 때
            list = noticeRepository.findByNoticeType(keyword, pageable);
        }

        return list.map(NoticeResponseDto::new);
    }

    @Transactional(readOnly = true)
    public List<NoticeResponseDto> findByKeywordTitle(String keyword) {

        List<NoticeEntity> list = noticeRepository.findByNoticeTitleContaining(keyword);

        return list.stream().map(NoticeResponseDto::new).collect(Collectors.toList());
    }

    // PK로 찾기
    @Transactional(readOnly = true)
    public NoticeResponseDto findById(Long noticeNo) {

        Optional<NoticeEntity> entity = noticeRepository.findById(noticeNo);
        if (!entity.isPresent()){
            return null;
        }

        return new NoticeResponseDto(entity.get());
    }

    // 수정
    @Transactional
    public Boolean update(final NoticeUpdateRequestDto dto) {

        Optional<NoticeEntity> entity = noticeRepository.findById(dto.getNoticeNo());
        if (!entity.isPresent()){
            return false;
        }

        entity.get().update(dto.getNoticeType(), dto.getNoticeTitle(), dto.getNoticeContent());
        return true;
    }

    // 생성
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

    // 삭제
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

    // 페이지 리스트 반환 (리스트 안 요소 개수는 항상 5)
    public List<Integer> getPageList(final int totalPage, final int page) {

        List<Integer> pageList = new ArrayList<>();

        if (totalPage <= 5){ // 전체 페이지 개수가 5이하일 때,
                             // ex. 전체 페이지가 3일 때, [0, 1, 2] 출력
            for (Integer i=0; i<=totalPage-1; i++){
                pageList.add(i);
            }
        }else if(page >= 0 && page <= 2){ // 전체 페이지 개수가 5초과이고, 현재 페이지가 0~2일 때,
                                          // ex. 현재 페이지가 1일 때, [0, 1, 2, 3, 4] 출력
            for (Integer i=0; i<=4; i++){
                pageList.add(i);
            }
        }
        else if (page >= totalPage-3 && page <= totalPage-1){ // 전체 페이지 개수가 5초과이고, 현재 페이지가 끝에서 0,1,2번째 일 때,
                                                              // ex. 전체 페이지 개수가 7이고, 현재 페이지가 5일 때, [2, 3, 4, 5, 6] 출력
            for (Integer i=5; i>=1; i--){
                pageList.add(totalPage - i);
            }
        }else{ // 그외일 때, 앞,뒤로(-2 ~ +2) 계산한 값 출력
            for (Integer i=-2; i<=2; i++){
                pageList.add(page + i);
            }
        }

        return pageList;
    }

}
