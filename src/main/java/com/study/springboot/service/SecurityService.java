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
        return new User(memberEntity.getUsername(), memberEntity.getPassword(), authorities);
    }

}
