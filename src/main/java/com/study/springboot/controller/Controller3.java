package com.study.springboot.controller;

import com.study.springboot.dto.security.MemberFindId;
import com.study.springboot.dto.security.MemberJoinDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.dto.review.ReviewSaveResponseDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.repository.MemberRepository;
import com.study.springboot.repository.ReviewRepository;
import com.study.springboot.service.ProductService;
import com.study.springboot.service.ReviewService;
import com.study.springboot.service.Service3;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class Controller3 {
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final MemberRepository memberRepository;
    final private PasswordEncoder passwordEncoder;
    private final Service3 service3;

    @RequestMapping("/admin/review")
    public String main(){
        return "redirect:/admin/review/list";
    }

    //list만 사용하는 전체 출력
    @RequestMapping("/admin/review/list")
    public String listForm( Model model,
                            @RequestParam(value = "dateStart", required = false) String dateStart,
                            @RequestParam(value = "dateEnd", required = false) String dateEnd,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "keyword", required = false ) String keyword,
                            @RequestParam(value = "findBy", required = false ) String findBy) throws ParseException {

        Page<ReviewResponseDto> list = null;
        int totalPage;
        List<Integer> pageList;
        if ((findBy == null) && (keyword == null) && (dateStart == null) && (dateEnd == null)
                || (dateStart.equals("null")) && (dateEnd.equals("null")) && (keyword.equals("null"))
                || (dateStart.equals("")) && (dateEnd.equals("")) && (keyword.equals(""))) {
            //페이징된 리스트 가져오기
            list = reviewService.getPageList(page);
        }else {
            if((!dateStart.equals("")) && (dateEnd.equals(""))){//dateStart 값만 있을때
                list = reviewService.findByDate(dateStart, page);
            }
            else if((!dateStart.equals("")) && (!dateEnd.equals(""))){//
                list = reviewService.findByDate(dateStart, dateEnd, page);
            }else {
                list = reviewService.findByKeyword(findBy, keyword, page);
            }
        }
        //출력페이지 5개로 고정
        totalPage = list.getTotalPages();
        pageList = reviewService.getPageList(totalPage, page);

        List<String> itemList = new ArrayList<>();
        for (ReviewResponseDto dto: list){
            String itemNo = dto.getItemNo();
            ProductResponseDto itemDto = productService.findById(Long.parseLong(itemNo));
            itemList.add(itemDto.getItemName());
        }

        model.addAttribute("itemList", itemList);
        model.addAttribute("list", list);
        model.addAttribute("findBy", findBy);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageList", pageList);
        model.addAttribute("dateStart", dateStart);
        model.addAttribute("dateEnd", dateEnd);
        //검색 상품 개수
        long listCount = reviewRepository.count();
        model.addAttribute("listCount", listCount);

        return "/admin/review/list";
    }
    //삭제하기
    @RequestMapping("/admin/review/delete")
    public String delete (@RequestParam("reviewNo") String reviewNo){
        reviewService.delete(reviewNo);
        return "redirect:/admin/review/list";
    }


    @ResponseBody
    @RequestMapping("admin/review/status/modify")
    public String reviewStatusModify(ReviewSaveResponseDto dto) {
        Long reviewNo = dto.getReviewNo();
        System.out.println("reviewNo = " + reviewNo);
        String reviewExpo = dto.getReviewExpo();
        System.out.println("reviewExpo = " + reviewExpo);
        boolean result = reviewService.statusModify(dto.getReviewNo(), dto.getReviewExpo());
        if (!result) {
            return "<script>alert('노출상태 변경 실패');location.href='/admin/review/list/';</script>";
        }
        return "<script>alert('노출상태 변경 완료');location.href='/admin/review/list/';</script>";
    }

    /////////////////  로그인 /////////////////////

    @GetMapping ("/")
    public String home(){
        return "user/user/userlogintest";
    }


    @GetMapping ("/user/login")
    public String login(){
        return "user/user/userlogin";
    }

    @GetMapping ("/user/join")
    public String join(){
        return "user/user/userjoin";
    }

//    @PostMapping("/user/loginAction")
//    @ResponseBody
//    public String loginAction(@Valid MemberLoginDto dto,
//                              BindingResult bindingResult,
//                              HttpServletRequest request) {
//        System.out.println("start");
//
//        if (bindingResult.hasErrors()) {
//            //DTO에 설정한 message값을 가져온다.
//            String detail = bindingResult.getFieldError().getDefaultMessage();
//            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
//            String bindResultCode = bindingResult.getFieldError().getCode();
//            System.out.println(detail + ":" + bindResultCode);
//            return "<script>alert('" + detail + "'); history.back();</script>";
//        }
//
//        System.out.println(dto.getUsername());
//        System.out.println(dto.getPassword());
//
//        //로그인 액션 처리 : 실제는 DB에 쿼리를 던진다.
//        List<MemberEntity> list = memberRepository.findByMemberIdAndMemberPw(
//                dto.getUsername(), dto.getPassword());
//
//        HttpStatus status = HttpStatus.OK;
//        if (list.isEmpty()) {
//            status = HttpStatus.NOT_FOUND;
//        } else {
//            status = HttpStatus.OK;
//        }
//        if (status == HttpStatus.OK) {
//            System.out.println("success!");
//
//            MemberEntity entity = list.get(0);
//            //로그아웃시까지 로그인한 회원정보(Member테이블)을 가지고 있다.
//            request.getSession().setAttribute("memberEntity", entity);
//            //세션객체에 로그인성공 값 저장(로그아웃시까지 저장됨.)
//            request.getSession().setAttribute("isLogin", true);
//            request.getSession().setAttribute("user_name", entity.getMemberName());
//            request.getSession().setAttribute("user_role", entity.getMemberRole());
//            return "<script>alert('로그인 성공');location.href='/';</script>";
//        } else {
//            return "<script>alert('로그인 실패'); history.back();</script>";
//        }
//
//    }
//
//    @GetMapping("/user/logout")
//    @ResponseBody
//    public String logoutAction(HttpServletRequest request) {
//        //세션 종료
//        request.getSession().invalidate();
//
//        return "<script>alert('로그아웃 되었습니다.'); location.href='/';</script>";
//    }

    @RequestMapping("/user/findId")
    @ResponseBody
    public String findId(@Valid MemberFindId dto,
                         BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            //DTO에 설정한 message값을 가져온다.
            String detail = bindingResult.getFieldError().getDefaultMessage();
            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError().getCode();
            System.out.println(detail + ":" + bindResultCode);
            return "<script>alert('" + detail + "'); history.back();</script>";
        }

        System.out.println(dto.getMemberName());
        System.out.println(dto.getMemberPhone());

        List<MemberEntity> list = memberRepository.findByMemberNameAndMemberPhone(
                dto.getMemberName(), dto.getMemberPhone());
        MemberEntity entity = list.get(0);

        String id = entity.getMemberId();

        if( id==null ){
            return "<script>alert('가입된 회원 정보가 없습니다. 이름/휴대폰번호를 확인해주세요.'); history.back();</script>";
        } else {
            return "<script>alert('회원님의 아이디는 "+id+" 입니다.'); location.href='/user/login';</script>";
        }
    }
    @PostMapping("/user/joinAction")
    @ResponseBody
    public String joinAction(@Valid MemberJoinDto dto, BindingResult bindingResult) {
        LocalDate today = LocalDate.now();
        System.out.println(today);
        dto.setMemberJoinDatetime(today);

        if( bindingResult.hasErrors() ) {
            //DTO에 설정한 message값을 가져온다.
            String detail = bindingResult.getFieldError().getDefaultMessage();
            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError().getCode();
            System.out.println( detail + ":" + bindResultCode);
            return "<script>alert('"+ detail +"'); history.back();</script>";
        }
        System.out.println( dto.getUsername() );
        System.out.println( dto.getPassword() );

        //암호화를 위해 시큐리티의 BCryptPasswordEncoder 클래스를 사용
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        System.out.println( "encodedPassword:" + encodedPassword );
        dto.setPassword( encodedPassword );
        //회원가입 DB 액션 수행
        try{
            MemberEntity enity = dto.toSaveEntity();
            memberRepository.save( enity );
        }
        catch (DataIntegrityViolationException e){
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "<script>alert('이미 등록된 사용자입니다.');history.back();</script>";
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "<script>alert('회원가입 실패했습니다.');history.back();</script>";
        }

        HttpStatus status = HttpStatus.OK;
        if( status == HttpStatus.OK ) {
            System.out.println("회원가입 성공!");
            return "<script>alert('회원가입 성공!'); location.href='/user/login';</script>";
        }else{
            return "<script>alert('회원가입 실패'); history.back();</script>";
        }
    }



    //글쓰기
//    @RequestMapping("/write")
//    public String write(){
//        return "/admin/review/writeForm";
//    }
//
//    @RequestMapping("/writeAction")
//    public String writeAction(ReviewSaveResponseDto dto){
//        reviewService.save(dto);
//        return "redirect:/admin/review/list";
//    }
//
//    //단건조회
//    @RequestMapping("/admin/review/modify")
//    public String modify(@RequestParam("reviewNo") int reviewNo,
//                         Model model) {
//        ReviewEntity review = reviewService.findById((long) reviewNo);
//
//        model.addAttribute("review", review);
//        return "/admin/review/modify";
//    }
//    //단건수정
//    @RequestMapping("/admin/review/modifyAction")
//    @ResponseBody
//    public String modifyAction(ReviewSaveResponseDto reviewSaveResponseDto){
//        boolean result = reviewService.update(reviewSaveResponseDto);
//        if(!result){
//            return "<script>alert('수정 실패');history.back();</script>";
//        }return "<script>alert('수정 완료');location.href='/admin/review/list';</script>";
//    }


}//class