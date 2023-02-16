package com.study.springboot.service;

import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.entity.Member.MemberEntity;
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

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

//    public void delete(final Long member_no) {
//        MemberEntity memberEntity = memberRepository.findById(member_no)
//                .orElseThrow() -> new IllegalArgumentException()
//    }

//    @GetMapping("/member-delete/{index}")
//    public String member_delete(@PathVariable("index") Integer index,
//                                Model model){
//
//        list.remove( index.intValue() );
//
//        return "redirect:/member/all";
//    }

//    =================================================================
//    @Transactional
//    public MemberEntity
public MemberResponseDto findById(long id) {
    MemberEntity entity = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    MemberResponseDto dto = new MemberResponseDto(entity);
    return dto;
}

@Transactional
public MemberResponseDto modify(final Long memberNo, final MemberResponseDto memberResponseDto){
    MemberEntity entity = memberRepository.findById(memberNo)
            .orElseThrow(() -> new IllegalArgumentException("없는 사용자"));

    entity.modify(memberResponseDto.getMemberName(),memberResponseDto.getMemberPhone(),
            memberResponseDto.getMemberAddrNumber(),memberResponseDto.getMemberAddr1(),
            memberResponseDto.getMemberAddr2(),memberResponseDto.getMemberEmail(),
            entity.getMemberNo());
//
//    String memberName, String memberPhone, String memberAddrNumber,
//            String memberAddr1, String memberAddr2,
//            String memberEmail,Long memberNo){


    System.out.println(entity.getMemberId()); // 서버저장확인
    
    MemberEntity newEntity = memberRepository.save(entity);
    MemberResponseDto dto = new MemberResponseDto(newEntity);
    return dto;
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

        if (findByType1.equals("all") && findByType2.equals("all")) {
            list = memberRepository.findByKeyword(keyword, pageable);
        } else if (!findByType1.equals("all") && findByType2.equals("all")) {
            list = memberRepository.findByType1(findByType1, keyword, pageable);
        } else if(findByType1.equals("all") && !findByType2.equals("all")) {

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


}