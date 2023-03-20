package com.study.springboot.service;

import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.member.MemberSaveRequestDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.security.MemberJoinDto;
import com.study.springboot.entity.*;
import com.study.springboot.entity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final ReviewRepository reviewRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final InquiryRepository inquiryRepository;
    private final QnaRepository qnaRepository;
    private final CartService cartService;
    private final QnaService qnaService;
    private final OrderService orderService;
    private final InquiryService inquiryService;

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
    }

    @Transactional(readOnly = true)
    public MemberEntity findByUserId(final String memberId){
        return memberRepository.findByUserId(memberId).get();
    }

    //회원탈퇴
    @Transactional
    public boolean exited(final String memberId) throws Exception {

        //회원이 작성했던 상품문의 삭제
        List<InquiryEntity> inquiryList = inquiryRepository.findByMemberId(memberId);
        for (int i = 0; i < inquiryList.size(); i++) {
            inquiryService.inquiryDelete(inquiryList.get(i).getInquiryNo());
        }

        //회원이 작성했던 리뷰 삭제
        List<ReviewEntity> reviewList =  reviewRepository.findByMemberIdContaining(memberId);
        for (int i = 0; i < reviewList.size(); i++) {
            ReviewEntity reviewEntity = reviewList.get(i);
            reviewRepository.delete(reviewEntity);
        }

        List<OrderEntity> orderList = cartService.findByOrderListEntity(memberId);
        List<OrderResponseDto> orderDtoList = cartService.findByOrderList(memberId);
        //회원 카트정보 삭제
        for (OrderResponseDto orderDto : orderDtoList) {
            List<CartEntity> cartEntityList = cartService.getCartEntityListMember(orderDto);
            for (int i = 0; i < cartEntityList.size(); i++) {
                cartRepository.delete(cartEntityList.get(i));
            }
        }
        //회원주문건 삭제
        for (int i = 0; i < orderList.size(); i++) {
            orderRepository.delete(orderList.get(i));
        }
        //회원이 작성했던 qna 삭제
        List<QnaEntity> qnaEntityList = qnaRepository.findByMemberId(memberId);
        for (int i = 0; i < qnaEntityList.size(); i++) {
            qnaService.delete(qnaEntityList.get(i).getQnaId());
        }

        MemberEntity memberEntity = memberRepository.findByUserId(memberId).get();
        try {
            memberRepository.delete(memberEntity);
            return true;

        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean update(final MemberJoinDto dto){
        try {
            MemberEntity enity = dto.toUpdateEntity();
            memberRepository.save( enity );

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Transactional(readOnly = true)
    public String findByMemberNameAndMemberPhone(final String memberName,final String memberPhone){
        Optional<MemberEntity> optional = memberRepository.findByMemberNameAndMemberPhone(memberName,memberPhone);
        return optional.get().getUsername();
    }

    //비밀번호 변경url이 있는 멜 보내기

    public boolean sendEmail(final String getEmail){
        try {
            String content =

            " <div style=\"height:100%;background:#fff;color:#000;font-size:15px;line-height:1.5;letter-spacing:-0.5px\">" +
            "<table style=\"text-align:left;table-layout:fixed;width:100%;border:0;border-spacing:0;border-collapse:collapse\">"+
            "<tbody>"+
            "<tr>"+
            "<td>"+
            "<div style=\"max-width:740px;border:1px solid #d7d7d7;letter-spacing:-1px\">"+
            "<div style=\"border-bottom:1px solid #d7d7d7;background:#f6f6f6\">"+
            "<h1 style=\"padding:20px 6%;margin:0\"><span style=\"display:block;font-size:20px;font-weight:normal;color:#000;word-break:break-word\">마이소호 샘플샵 비밀번호 변경 링크 안내 이메일입니다</span></h1>"+
            "</div>"+
            "<div style=\"padding:4% 6%\">"+
            "<h2 style=\"font-size:32px;color:#00b0ff;word-break:break-word!important\">마이소호 샘플샵 비밀번호 변경 </h2>"+
            "<div>"+
            "<p style=\"margin-bottom:25px;font-size:17px;color:#454545;line-height:25px;word-break:break-word!important\">"+
            "안녕하세요. 마이소호 샘플샵입니다.  <br>"+
            "비밀번호 변경을 원하실 경우<br>"+
            "하단 비밀번호 변경하기를 눌러주세요."+
            "<br><br>"+
            "<a href=\"http://localhost:8080/find/password2?getEmail="+getEmail+"\" rel=\"noopener\" style=\"display:inline-block;width:300px;height:50px;line-height:52px;background:#333;box-sizing:border-box;color:#fff;font-size:16px;font-weight:bold;text-align:center;text-decoration:none\" target=\"_blank\" >비밀번호 변경 하기</a>"+
            "<br><br>"+
            "</p>"+
            "</div>"+
            "</div>"+
            "<div style=\"overflow:hidden;height:auto;padding:10px 0;border-top:1px solid #d7d7d7;background:#f6f6f6\">"+
            "<div style=\"width:100%;margin-right:15px;font-size:13px;color:#000;line-height:26px;letter-spacing:0;padding:0 6%\">"+
            "<p style=\"margin:0;padding:0\">서울 금천구 가산디지털1로 168  (가산동,우림라이온스밸리)A동 14층   |   사업자번호  2068121131</p>"+
            "<p style=\"margin:0;padding:0\">02-6903-9118   |   <a href=\"mailto:help@mysoho.com\" style=\"color:#0000ff!important;text-decoration:none\" target=\"_blank\">help@mysoho.com</a></p>"+
            "</div>"+
            "</div>"+
            "</div>"+
            "</td>"+
            "</tr>"+
            "</tbody>"+
            "</table><div class=\"yj6qo\"></div><div class=\"adL\">"+
            "</div></div>";

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
            mimeMessageHelper.setTo(getEmail);
            mimeMessageHelper.setFrom("mysoho2023@naver.com");
            mimeMessageHelper.setSubject("마이소호 샘플샵 비밀번호 변경 안내 이메일입니다");
            mimeMessageHelper.setText(content,true);
            javaMailSender.send(mimeMessage);
            return true;
        }catch (MessagingException e){
            e.printStackTrace();
            return false;
        }

    }

    //비밀번호 변경하기
    @Transactional
    public boolean changePassword(final String getEmail,final String password){

        Optional<MemberEntity> optional = memberRepository.findByMemberEmail(getEmail);

        if(!optional.isPresent()){

            System.out.println("memberid is not present");

        }
        try {
            String encodedPassword = passwordEncoder.encode(password);
            MemberEntity entity = optional.get();
            entity.updatePassword(encodedPassword);
            memberRepository.save( entity );
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Transactional(readOnly = true)
    public String findByMemberEmail(final String email){
        Optional<MemberEntity> optional = memberRepository.findByMemberEmail(email);
        return optional.get().getUsername();
    }


    @Transactional
    public void saveMileage(String memberId, Long mileage) {
        MemberResponseDto memberResponseDto = findByMemberId(memberId);
        memberResponseDto.setMemberMileage(memberResponseDto.getMemberMileage()+mileage);
        memberRepository.save(memberResponseDto.toUpdateEntity());
    }
}
