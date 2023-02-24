package com.study.springboot.service;

import com.study.springboot.entity.MemberEntity;
import com.study.springboot.repository.MemberRepository;
import com.study.springboot.enumeration.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SecurityService implements UserDetailsService {

    final private MemberRepository memberRepository;

    //username(id)을 통해, 스프링 시큐리티에 사용자 정보(password,role)를 전달한다.
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        //USER/ADMIN권한(역할)등을 넣어줌
//        authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));//권한은 ROLE로 시작됨.
//        //암호 "1234"문자열을 Bcrypt암호화(bcrypt-generator.com)에서 암호 생성하여 넣는다.//username,password,role반환
//        return new User("hong","$2a$12$z/2MFyi/C/mGA.w48Z2LzOdmfN2npyEFMLSFlJGRElc3bnE0CIuK6",authorities);
//    }
    //db에서 데이타 가져오기


//        @Override
//        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            //ADMIN 권한/역할을 넣는다.
//            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
//            //“1234”문자열을 Bcrypt 사이트(bcrypt-generator.com)에서 암호 생성하여 넣는다.
//            return new User("hong", "$2a$12$CLFNXQConBP9WhVNqpWYY.5RmFID66xYzDI8yOFRf.RC/Qac41QjG", authorities);
//        }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<MemberEntity> _optMemberEntity = this.memberRepository.findByUserId( username );
        if (_optMemberEntity.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        MemberEntity memberEntity = _optMemberEntity.get();
        String memberRole = memberEntity.getMemberRole();
        System.out.println(memberRole);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ( memberRole.contains("ADMIN")) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        return new User(memberEntity.getMemberId(), memberEntity.getMemberPw(), authorities);
    }

}
