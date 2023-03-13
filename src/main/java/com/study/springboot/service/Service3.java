package com.study.springboot.service;

import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.dto.security.MemberJoinDto;
import com.study.springboot.entity.*;
import com.study.springboot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class Service3 {
    final private MemberRepository memberRepository;
    final private JavaMailSender javaMailSender;
    final private PasswordEncoder passwordEncoder;
    final ReviewRepository reviewRepository;
    final CartRepository cartRepository;
    final OrderRepository orderRepository;
    final InquiryRepository inquiryRepository;
    final QnaRepository qnaRepository;
    final CartService cartService;
    final Service1 service1;
    final Service2 service2;
    final QnaService qnaService;

    @Transactional(readOnly = true)
    public MemberEntity findByUserId(final String memberId){
        Optional<MemberEntity> optional = memberRepository.findByUserId(memberId);
        return optional.get();
    }

    //선교 추가
    @Transactional
    public boolean exited(final String memberId) throws Exception {

        //회원이 작성했던 상품문의 삭제
        List<InquiryEntity> inquiryList = inquiryRepository.findByMemberId(memberId);
        for (int i = 0; i < inquiryList.size(); i++) {
            service2.delete(inquiryList.get(i).getInquiryNo());
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
            List<CartEntity> cartEntityList = service1.getCartEntityListMember(orderDto);
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
            System.out.println(enity.getMemberName());
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
                    "   <table border=\"1px\" align=\"center\">\n" +
                            "    <tr align=\"center\">\n" +
                            "      <td><h3 style=\"color:skyblue\">마이소호 샘플샵 비밀번호 변경</h3></td>\n" +
                            "    </tr>\n" +
                            "    <tr>\n" +
                            "      <td>\n" +
                            "        <div>안녕하세요. 마이소호 샘플샵입니다 비밀번호 변경하기를 눌러주세요<div>\n" +
                            "        <div><a href=\"http://localhost:8080/find/password2?getEmail="+getEmail+"\">\n" +
                            "        <input type=\"submit\" role=\"button\" value=\"비밀번호 변경하기\" /></a></div>\n" +
                            "      </td>\n" +
                            "    </tr>\n" +
                            "    <tr>\n" +
                            "      <td>\n" +
                            "        <div>서울 금천구 가산디지털 1로 168(가산동, 우림라이온스밸리)A동 14층</div>\n" +
                            "        <div>사업자번호 206812113102-6903-9118 | mysoho2023@naver.com</div>\n" +
                            "      </td>\n" +
                            "    </tr>\n" +
                            "   </table>";
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
            System.out.println("encodedPassword:"+encodedPassword);
            MemberEntity entity = optional.get();
            entity.updatePassword(encodedPassword);
            memberRepository.save( entity );
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //리뷰
    public void getList(final String memberId,final String orderState){
        List<OrderEntity> orderList = orderRepository.findByMemberIdAndOrderState(memberId,"배송완료");
    }


}//class