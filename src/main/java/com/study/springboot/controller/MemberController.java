package com.study.springboot.controller;

import com.study.springboot.dto.MemberJoinDto;
import com.study.springboot.dto.MemberLoginDto;
import com.study.springboot.entity.Member;
import com.study.springboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    final MemberRepository memberRepository;//생성자 주입
    
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @PostMapping("/loginAction")
    @ResponseBody
    public String loginAction(@Valid MemberLoginDto dto,
                              BindingResult bindingResult,
                              HttpServletRequest request){
        System.out.println("id = " + dto.getUser_id());
        System.out.println("pw = " + dto.getUser_pw());

        if (bindingResult.hasErrors()) {
            //DTO에 설정한 message값을 가져온다.
            String detail = bindingResult.getFieldError().getDefaultMessage();
            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bidnResultCode = bindingResult.getFieldError().getCode();
            System.out.println("detail = " + detail);
            System.out.println("bidnResultCode = " + bidnResultCode);
            return "<script>alert('" + detail + "');history.back();</script>";
        }

        //로그인 액션 처리 : DB에 쿼리를 던진다.
        List<Member> list = memberRepository.findByUserIdAndUserPw(
                dto.getUser_id(), dto.getUser_pw());

        
        HttpStatus status = HttpStatus.NOT_FOUND;
        if (list.isEmpty()) {
            status = HttpStatus.NOT_FOUND;
        }else {
            status = HttpStatus.OK;
        }
        if (status == HttpStatus.OK) {
            System.out.println("로그인 성공");

            //세션객체에 로그인 성공 값 저장 (로그아웃까지 저장됨)
            request.getSession().setAttribute("isLogin", true);
            request.getSession().setAttribute("user_id", dto.getUser_id());
            return "<script>alert('로그인 성공');location.href='/'</script>";
        } else {
            return "<script>alert('로그인 실패');history.back();</script>";
        }

    }

    @GetMapping("/logoutAction")
    @ResponseBody
    public String logoutAction(HttpServletRequest request) {
        //세션종류
        request.getSession().invalidate();
        return "<script>alert('로그아웃 되었습니다.');location.href='/'</script>";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }


    @PostMapping("/joinAction")
    @ResponseBody
    public String joinAction(@Valid MemberJoinDto dto,
                             BindingResult bindingResult,
                             Model model){
        if (bindingResult.hasErrors()) {
            //DTO에 설정한 message값을 가져온다.
            String detail = bindingResult.getFieldError().getDefaultMessage();
            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bidnResultCode = bindingResult.getFieldError().getCode();
            System.out.println("detail = " + detail);
            System.out.println("bidnResultCode = " + bidnResultCode);
            return "<script>alert('" + detail + "');history.back();</script>";
        }

        //회원가입 DB 액션 수행
        //기존에 같은 아이디의 회원이 있는지 중복체크 해야 함
        //select * from member where user_id='' //아이디의 검색 결과가 1 이상이면 중복회원임
        //insert into member user_id='', user_pw='';
        //지금은 무조건 성공 시킴
        HttpStatus status = HttpStatus.OK;
        if (status == HttpStatus.OK) {
            System.out.println("회원가입 성공");
            return "<script>alert('회원가입 성공');location.href='/loginForm'</script>";
        } else {
            return "<script>alert('회원가입 실패');history.back();</script>";
        }


    }
}
