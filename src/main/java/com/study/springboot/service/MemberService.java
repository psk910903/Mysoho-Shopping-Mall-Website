package com.study.springboot.service;

import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.member.MemberSaveRequestDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.repository.MemberRepository;
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

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

public MemberResponseDto findById(long id) {
    MemberEntity entity = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    MemberResponseDto dto = new MemberResponseDto(entity);
    return dto;
}

@Transactional
public boolean modify(MemberSaveRequestDto dto){

    MemberEntity entity = memberRepository.findById(dto.getMemberNo()).get();
    dto.setMemberJoinDatetime(entity.getMemberJoinDatetime());
    try {
        memberRepository.save(dto.toUpdateEntity());
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
    return true;
}


    @Transactional
    public void delete(final Long memberNo){
        MemberEntity entity = memberRepository.findById(memberNo)
                .orElseThrow( ()-> new IllegalArgumentException("해당 회원이 없습니다"));
        memberRepository.delete(entity);
        
    }

    @Transactional(readOnly = true)
    public Page<MemberResponseDto> findAll(int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("memberNo"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<MemberEntity> list = memberRepository.findAll(pageable);

        return list.map(MemberResponseDto::new);
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

    @Transactional(readOnly = true)
    public Page<MemberResponseDto> findByKeyword(String findByType1, String findByType2, String keyword, int page) {

        Page<MemberEntity> list;
        Pageable pageable = PageRequest.of(page, 10);


        if (findByType1.equals("all") && findByType2.equals("all")) { //키워드만 입력됬을 때
            list = memberRepository.findByKeyword(keyword, pageable);
        } else if (!findByType1.equals("all") && findByType2.equals("all")) { //회원등급 & 키워드가 입력됬을 때
            list = memberRepository.findByType1(findByType1, keyword, pageable);
        } else if(findByType1.equals("all") && !findByType2.equals("all")) { //이름,아이디 & 키워드가 입력됬을 때

            if (findByType2.equals("아이디")) {
                list = memberRepository.findByMemberId(keyword, pageable);
            } else  { //(findByType2.equals("이름"))
                list = memberRepository.findByMemberName(keyword, pageable);
            }
        } else{ //(!findByType1.equals("all") && !findByType2.equals("all"))
            if (findByType2.equals("이름")) {
                list = memberRepository.findByMemberName(findByType1, keyword, pageable);
            } else{ //(findByType2.equals("아이디"))
                list = memberRepository.findByMemberId(findByType1, keyword, pageable);
            }
        }

        return list.map(MemberResponseDto::new);
    }


    @Transactional
    public void deleteCheck(String arrStr){
        String[] arrIdx = arrStr.split(",");
        for (int i = 0; i < arrIdx.length; i++) {
            try {
                MemberEntity entity = memberRepository.findById((long) Integer.parseInt(arrIdx[i])).get();
                memberRepository.delete(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findByMemberId(String memberId){ // 이름 바꿔야함
        Optional<MemberEntity> entity = memberRepository.findByMemberId(memberId);
        if (!entity.isPresent()){
            return null;
        }
        return new MemberResponseDto(entity.get());
    };


}
