package com.study.springboot.service;


import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.security.MemberJoinDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.entity.OrderEntity;
import com.study.springboot.entity.ReviewEntity;
import com.study.springboot.repository.MemberRepository;

import com.study.springboot.repository.OrderRepository;
import com.study.springboot.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class Service3 {
    final private MemberRepository memberRepository;
    final private ReviewRepository reviewRepository;
    final private JavaMailSender javaMailSender;
    final private PasswordEncoder passwordEncoder;

    final private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public MemberEntity findByUserId(final String memberId){
        Optional<MemberEntity> optional = memberRepository.findByUserId(memberId);
        return optional.get();
    }

    @Transactional
    public boolean exited(final String username) throws Exception {
        Optional<MemberEntity> optional = memberRepository.findByUserId(username);
        if( !optional.isPresent() ) {
            throw new Exception("member id is not present!");
        }
        try {
            memberRepository.delete(optional.get());
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

//    @Transactional
//    public String changPassword(final String getEmail){
//        //새 암호 만들기
//        String tempPassword = getTempPassword();
//        //회원 찾기
//        Optional<MemberEntity> optional = memberRepository.findByMemberEmail(getEmail);
//        if(optional == null){
//            return "1";//회원없음
//        }else {
//            boolean result = sendEmail(getEmail,tempPassword);
//            if(!result){
//                return "2";//멜 보내기 실패
//            }else {
//                //비크립트화
//                String encodedPassword = passwordEncoder.encode(tempPassword);
//                //entity에 비크립트화된 암호 저장
//                MemberEntity entity = optional.get();
//                entity.updatePassword(encodedPassword);
//                return "0";//성공
//            }
//        }
//    }
//
//    //멜 보내기
//    public boolean sendEmail(final String getEmail,final String tempPassword){
//        try {
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
//            mimeMessageHelper.setTo(getEmail);
//            mimeMessageHelper.setFrom("mysoho2023@naver.com");
//            mimeMessageHelper.setSubject("mysoho 비밀번호 안내");
//            mimeMessageHelper.setText("고객님의 새 비밀번호는"+tempPassword+" 입니다. 로그인 후 비밀번호를 변경하세요");
//            javaMailSender.send(mimeMessage);
//            return true;
//        }catch (MessagingException e){
//            e.printStackTrace();
//            return false;
//        }
//    }
    //비밀번호 변경url이 있는 멜 보내기

    public boolean sendEmail(final String getEmail){

        try {

            String content =

                    "   <table border=\"1px\" align=\"center\">\n" +

                            "    <tr>\n" +

                            "      <td><h3 style=\"color:skyblue\">마이소호 샘플샵 비밀번호 변경</h3></td>\n" +

                            "    </tr>\n" +

                            "    <tr>\n" +

                            "      <td>\n" +

                            "        <div>안녕하세요. 마이소호 샘플샵입니다 비밀번호 변경하기를 눌러주세요<div>\n" +

                            "        <div><a href=\"http://localhost:8080/find/password2?getEmail="+getEmail+"\">\n" +

                            "        <button type=\"submit\">비밀번호 변경하기</button></a></div>\n" +

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

    public boolean changePassword(final String getEmail,final String password){

        Optional<MemberEntity> optional = memberRepository.findByMemberEmail(getEmail);

        if(!optional.isPresent()){

            System.out.println("memberid is not present");

        }

        try {

            String encodedPassword = passwordEncoder.encode(password);

            System.out.println("encodedPassword:"+encodedPassword);

            MemberEntity entity = optional.get();

            System.out.println(entity.getUsername());

            entity.updatePassword(encodedPassword);

            System.out.println(entity.getPassword());

            return true;

        }catch (Exception e){

            e.printStackTrace();

            return false;

        }

    }

    //임의로 패스워드 만들기
    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String password = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            password += charSet[idx];
        }
        return password;
    }

    //리뷰
    public void getList(final String memberId,final String orderState){
        List<OrderEntity> orderList = orderRepository.findByMemberIdAndOrderState(memberId,"배송완료");

    }


}//class