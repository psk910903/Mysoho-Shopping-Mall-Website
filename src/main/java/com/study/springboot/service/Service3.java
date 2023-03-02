package com.study.springboot.service;


import com.study.springboot.dto.security.MemberJoinDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.Optional;
@RequiredArgsConstructor
@Service
public class Service3 {
    final private MemberRepository memberRepository;
    final private JavaMailSender javaMailSender;
    final private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MemberEntity findByUserId(final String memberId){
        Optional<MemberEntity> optional = memberRepository.findByUserId(memberId);
        return optional.get();
    }

    @Transactional
    public boolean delete(final String username) throws Exception {
        Optional<MemberEntity> optional = memberRepository.findByUserId(username);
        if( !optional.isPresent() ) {
            throw new Exception("member id is not present!");
        }
        MemberEntity entity = optional.get();
        try {
            memberRepository.delete( entity );
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
        return true;
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

    @Transactional
    public String changPassword(final String getEmail){
        //새 암호 만들기
        String tempPassword = getTempPassword();
        //회원 찾기
        Optional<MemberEntity> optional = memberRepository.findByMemberEmail(getEmail);
        if(optional == null){
            return "1";//회원없음
        }else {
            boolean result = sendEmail(getEmail,tempPassword);
            if(!result){
                return "2";//멜 보내기 실패
            }else {
                //비크립트화
                String encodedPassword = passwordEncoder.encode(tempPassword);
                //entity에 비크립트화된 암호 저장
                MemberEntity entity = optional.get();
                entity.updatePassword(encodedPassword);
                memberRepository.save(entity);
                return "0";
            }
        }
    }

    //멜 보내기
    public boolean sendEmail(final String getEmail,final String tempPassword){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
            mimeMessageHelper.setTo(getEmail);
            mimeMessageHelper.setFrom("mysoho2023@naver.com");
            mimeMessageHelper.setSubject("mysoho 비밀번호 안내");
            mimeMessageHelper.setText("고객님의 새 비밀번호는"+tempPassword+" 입니다. 로그인 후 비밀번호를 변경하세요");
            javaMailSender.send(mimeMessage);
            return true;
        }catch (MessagingException e){
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


}//class