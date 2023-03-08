package com.study.springboot.controller;

import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.cart.CartSaveRequestDto;
import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.notice.NoticeResponseDto;
import com.study.springboot.dto.notice.NoticeSaveRequestDto;
import com.study.springboot.dto.notice.NoticeUpdateRequestDto;
import com.study.springboot.dto.order.OrderContentSaveRequestDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.dto.security.MemberJoinDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.object.FileResponse;
import com.study.springboot.repository.MemberRepository;
import com.study.springboot.repository.NoticeRepository;
import com.study.springboot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Controller
public class Controller2 {

    // '/admin/notice' 시작 -----------------------------------------------------------------------------------------------

    private final NoticeService noticeService;
    private final AwsS3Service awsS3Service;
    private final NoticeRepository noticeRepository;

    private final Service2 service2;


    // URL: localhost8080:/admin/notice
    @GetMapping("/admin/notice")
    public String noticeHome(){
        return "redirect:/admin/notice/list?page=0"; // localhost8080:/admin/notice/list로 redirect
    }

    // URL: localhost8080:/admin/notice/list
    // 공지글 리스트 페이지
    @GetMapping("/admin/notice/list")
    public String noticeList(@RequestParam(value = "findBy", required = false) String findBy,   // findBy : type(종류), title(제목), content(내용)에 따라 글 분류
                       @RequestParam(value = "keyword", required = false) String keyword, // keyword: 어떤 keyword로 찾을 것인지 결정
                       @RequestParam(value = "page", defaultValue = "0") int page,        // page: 0에서부터 시작
                        Model model) {                                                    // ex) findBy=title, keyword="키워드입니다", page:2면
                                                                                          //     제목에서 "키워드입니다"가 포함된 글 중 3쪽을 보여줌
        Page<NoticeResponseDto> list;
        if (findBy == null) { // 검색 기능을 쓰지 않을 때
            list = noticeService.findAll(page);
        } else{ // 검색 기능을 쓸 때
            list = noticeService.findByKeyword(findBy, keyword, page);
        }

        int totalPage = list.getTotalPages(); // 전체 페이지 개수
        List<Integer> pageList = noticeService.getPageList(totalPage, page); // 해당 page에서 아래쪽 페이지바에 보이는 숫자 list

        model.addAttribute("list", list);
        model.addAttribute("findBy", findBy);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageList", pageList);

        long listCount = noticeRepository.count();
        model.addAttribute("listCount", listCount);

        return "admin/notice/list"; //listForm.html로 응답

    }

    // URL: localhost8080:/admin/notice/content/{정수}
    // 상세 페이지
    @GetMapping("/admin/notice/content/{noticeNo}")
    public String noticeContent(@PathVariable("noticeNo") Long noticeNo, Model model) {

        NoticeResponseDto dto = noticeService.findById(noticeNo);
        if (dto == null){
            return "redirect:/admin/notice/list";
        }

        model.addAttribute("notice", dto);

        return "admin/notice/content"; //content.html로 응답
    }

    // URL: localhost8080:/admin/notice/write
    // 새 글 작성 페이지
    @GetMapping("/admin/notice/write")
    public String noticeWrite(Model model) {

        NoticeResponseDto dto = NoticeResponseDto.builder()
                .noticeType("공지사항")
                .noticeTitle("")
                .noticeContent("")
                .build();

        model.addAttribute("notice",dto);

        return "admin/notice/write";//write.html로 응답
    }

    // URL: localhost8080:/admin/notice/modify/{정수}
    // 기존 글 수정 페이지
    @GetMapping("/admin/notice/modify")
    public String noticeModify(@RequestParam("noticeNo") Long noticeNo, Model model) {

        NoticeResponseDto dto = noticeService.findById(noticeNo);
        if (dto == null){
            return "redirect:/admin/notice/list";
        }

        model.addAttribute("notice",dto);

        return "admin/notice/write"; //write.html로 응답
    }

    // 수정된 기존 글 데이터 데이터베이스에 넣기
    @PostMapping("/admin/notice/modifyAction")
    @ResponseBody
    public String noticeModifyAction(NoticeUpdateRequestDto dto) {

        if(dto.getNoticeTitle().length() > 250){
            return "<script>alert('제목은 250자 이내로 써주세요.'); history.back();</script>";
        }

        Boolean success = noticeService.update(dto);
        if(success) {
            return "<script>alert('게시글 수정 완료'); location.href='/admin/notice/content/" + dto.getNoticeNo() + "';</script>";
        }else{
            return "<script>alert('게시글 수정 실패'); history.back();</script>";
        }
    }

    // 작성된 새로운 글 데이터 데이터베이스에 넣기
    @PostMapping("/admin/notice/writeAction")
    @ResponseBody
    public String noticeWriteAction(NoticeSaveRequestDto dto) {

        if(dto.getNoticeTitle().length() > 250){
            return "<script>alert('제목은 250자 이내로 써주세요.'); history.back();</script>";
        }

        Boolean success = noticeService.save(dto);
        if(success) {
            return "<script>alert('게시글 등록 완료'); location.href='/admin/notice/list';</script>";
        }else{
            return "<script>alert('게시글 등록 실패'); history.back();</script>";
        }
    }

    // 기존 글 데이터베이스에서 삭제하기
    @GetMapping("/admin/notice/deleteAction")
    @ResponseBody
    public String noticeDeleteAction(@RequestParam("noticeNo") Long noticeNo) {

        Boolean success = noticeService.delete(noticeNo);
        if(success) {
            return "<script>alert('게시글 삭제 완료'); location.href='/admin/notice/list';</script>";
        }else{
            return "<script>alert('게시글 삭제 실패'); history.back();</script>";
        }

    }

    // 글 작성 시 이미지 업로드 할 때 awsS3에 이미지를 넣고 이미지 url 반환 (ckeditor로 이동)
    @PostMapping("/find/admin/notice/imgUpload")
    @ResponseBody
    public ResponseEntity<FileResponse> noticeImgUpload(
            @RequestPart(value = "upload", required = false) MultipartFile fileload) throws Exception {

        return new ResponseEntity<>(FileResponse.builder().
                uploaded(true).
                url(awsS3Service.upload(fileload)).
                build(), HttpStatus.OK);
    }

    // '/admin/notice' 끝 -----------------------------------------------------------------------------------------------
    // '/notice' 시작 -----------------------------------------------------------------------------------------------

    @RequestMapping(value = "/notice", method =  {RequestMethod.GET, RequestMethod.POST})
    public String notice( @RequestParam(value = "keyword", required = false) String keyword,
                          Model model) {

        List<NoticeResponseDto> list;
        if (keyword == null) { // 검색 기능을 쓰지 않을 때
            list = noticeService.findAll();
        } else{ // 검색 기능을 쓸 때
            list = noticeService.findByKeywordTitle(keyword);
        }

        model.addAttribute("list", list);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listCount", list.size());

        return "user/category/notice";
    }

    // 나중에 함수이름 noticeContent로 바꾸기
    @GetMapping("/notice/{noticeNo}")
    public String notice( @PathVariable("noticeNo") Long noticeNo,
                          Model model) {

        NoticeResponseDto dto = noticeService.findById(noticeNo);
        model.addAttribute("dto", dto);

        return "user/category/notice-content";
    }
    // '/notice' 끝 -----------------------------------------------------------------------------------------------
    // '/inquiry/myProductInquiries' 시작 -----------------------------------------------------------------------------------------------

    private final ProductService productService;

    @GetMapping("/inquiry/myProductInquiries")
    public String inquiryMyProductInquiries(Model model, @AuthenticationPrincipal User user) {

        String memberId = user.getUsername();

        List<InquiryResponseDto> inquiryList = service2.findByMemberId(memberId);
        List<ProductResponseDto> itemList = new ArrayList<>();
        List<Long> replyCountList = new ArrayList<>();

        for(InquiryResponseDto inquiryDto : inquiryList) {
            // itemList
            Long itemNo = inquiryDto.getItemNo();
            ProductResponseDto productDto = productService.findById(itemNo);
            itemList.add(productDto);

            // replyCountList
            Long replyCount = service2.countByInquiryNo(inquiryDto.getInquiryNo());
            replyCountList.add(replyCount);

        }

        // memberHiddenName
        String memberName = service2.findMemberNameByMemberId(memberId);
        String memberHiddenName;

        if (memberName.length() <= 2){
            memberHiddenName = memberName;
        }
        else{
            memberHiddenName = memberName.substring(0,2);
            for (int i=0; i<memberName.length()-2; i++){
                memberHiddenName += "*";
            }
        }

        model.addAttribute("itemList", itemList);
        model.addAttribute("inquiryList", inquiryList);
        model.addAttribute("replyCountList", replyCountList);
        model.addAttribute("memberHiddenName", memberHiddenName);

        return "user/user/myProductInquiries";
    }

    @PostMapping("/inquiry/myProductInquiries/deleteAction")
    @ResponseBody
    public String inquiryMyProductInquiriesDeleteAction(@RequestParam Long inquiryNo) {

        Boolean success = service2.delete(inquiryNo);
        if(success) {
            return "<script>alert('삭제되었습니다.'); location.href='/inquiry/myProductInquiries';</script>";
        }else{
            return "<script>alert('삭제 실패했습니다.'); history.back();</script>";
        }


    }

    // '/inquiry/myProductInquiries' 끝 -----------------------------------------------------------------------------------------------
    // '/qna/user' 시작 -----------------------------------------------------------------------------------------------

    final QnaService qnaService;

    @GetMapping("/qna/user")
    public String qnaUser(Model model,
                          @AuthenticationPrincipal User user) {
        String memberId = user.getUsername();
        List<QnaResponseDto> qnaList = service2.findByMemberIdQna(memberId);
        List<Long> replyCountList = new ArrayList<>();

        for(QnaResponseDto qnaDto : qnaList) {
            // replyCountList
            Long replyCount = service2.countByQnaId(qnaDto.getQnaId());
            replyCountList.add(replyCount);
        }

        // memberHiddenName
        String memberName = service2.findMemberNameByMemberId(memberId); // "홍길동임"
        String memberHiddenName; // "홍길**"

        if (memberName.length() <= 2){
            memberHiddenName = memberName;
        }
        else{
            memberHiddenName = memberName.substring(0,2);
            for (int i=0; i<memberName.length()-2; i++){
                memberHiddenName += "*";
            }
        }

        model.addAttribute("qnaList", qnaList);
        model.addAttribute("replyCountList", replyCountList);
        model.addAttribute("memberHiddenName", memberHiddenName);

        return "user/user/qna-user";
    }

    @PostMapping("/qna/user/deleteAction")
    @ResponseBody
    public String qnaUserDeleteAction(@RequestParam Long qnaId) {

        Boolean success = qnaService.delete(qnaId);
        if(success) {
            return "<script>alert('삭제되었습니다.'); location.href='/qna/user';</script>";
        }else{
            return "<script>alert('삭제 실패했습니다.'); history.back();</script>";
        }


    }

    // '/qna/user' 끝 -----------------------------------------------------------------------------------------------
    // '/order' 시작 -----------------------------------------------------------------------------------------------

    final private MemberService memberService;

    @GetMapping("/order")
    public String order(Model model, HttpServletRequest request, @AuthenticationPrincipal User user) {

        List<ProductResponseDto> itemList = new ArrayList<>();
        List<CartResponseDto> cartList = new ArrayList<>();

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기

        // 쿠키 정렬하기
        Arrays.sort(cookies, new Comparator<Cookie>() {
            @Override
            public int compare(Cookie c1, Cookie c2) {
                String c1Name = c1.getName();
                String c2Name = c2.getName();

                return c2Name.compareTo(c1Name);
            }
        });


        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기

                if (name.startsWith("item_idx.")) {

                    // 변수 선언
                    Long itemNo = Long.parseLong(name.split("\\.")[1]);
                    Long cartItemAmount = Long.parseLong(value);
                    String itemOptionColor = "";
                    try {
                        itemOptionColor = URLDecoder.decode(name.split("\\.")[2], "UTF-8");
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    String itemOptionSize = name.split("\\.")[3];

                    // itemList
                    ProductResponseDto productResponseDto = productService.findById(itemNo);
                    itemList.add(productResponseDto);

                    // cartList
                    String cartCode = UUID.randomUUID().toString();
                    Long cartDiscountPrice = productResponseDto.getItemPrice() * productResponseDto.getItemDiscountRate() / 100;
                    Long cartItemPrice = (productResponseDto.getItemPrice() - cartDiscountPrice) /100 * 100;
                    cartDiscountPrice = productResponseDto.getItemPrice() - cartItemPrice;

                    CartResponseDto cartResponseDto = CartResponseDto.builder()
                            .cartCode(cartCode)
                            .itemCode(String.valueOf(itemNo))
                            .itemName(productResponseDto.getItemName())
                            .itemOptionColor(itemOptionColor)
                            .itemOptionSize(itemOptionSize)
                            .cartItemAmount(cartItemAmount)
                            .cartItemOriginalPrice(productResponseDto.getItemPrice())
                            .cartDiscountPrice(cartDiscountPrice)
                            .cartItemPrice(cartItemPrice)
                            .build();
                    cartList.add(cartResponseDto);

                }
            }
        }

        // member
        MemberResponseDto memberResponseDto = null;
        if (user != null) {
            String memberId = user.getUsername();
            memberResponseDto = service2.findByMemberIdMember(memberId);
        }

        model.addAttribute("itemList", itemList);
        model.addAttribute("cartList", cartList);
        model.addAttribute("member", memberResponseDto);

        return "/user/order/shopping-basket";

    }

    @PostMapping("/order/deleteAllAction")
    @ResponseBody
    public String orderDeleteAllAction(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie c : cookies){
                String name = c.getName();

                if (name.startsWith("item_idx.")) {
                    c.setPath("/");
                    c.setMaxAge(0);
                    response.addCookie(c);
                }
            }
        }

        return "<script>alert('장바구니가 비었습니다.\\n관심있는 상품을 담아보세요.');location.href='/';</script>";
    }

    @PostMapping("/order/deleteAction")
    @ResponseBody
    public String orderDeleteAction(@RequestParam String itemOptionColor, @RequestParam String itemOptionSize,
                                    @RequestParam String itemNo, HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        String encodedItemOptionColor = itemOptionColor;
        int cartNum = 0;

        try {
            encodedItemOptionColor = URLEncoder.encode(itemOptionColor, "UTF-8");
        }catch (Exception e) {
            e.printStackTrace();
            return "<script>alert('삭제 중 오류가 발생했습니다.\\n다시 입력해주세요.'); location.href='/order';</script>";
        }

        if(cookies!=null){
            for(Cookie c : cookies){
                String name = c.getName();
                String value = c.getValue();
                if (name.startsWith("item_idx.")){
                    cartNum++;
                }
                if (name.equals("item_idx."+ itemNo + "." + encodedItemOptionColor + "." + itemOptionSize)) {
                    Cookie cookie = new Cookie(name, value);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }

        // 장바구니 비었을 떄
        if (cartNum == 1){
            return "<script>alert('장바구니가 비었습니다.\\n관심있는 상품을 담아보세요.');location.href='/';</script>";
        }

        return "<script>location.href='/order';</script>";
    }

    @PostMapping("/order/modifyAction")
    @ResponseBody
    public String orderModifyAction(@RequestParam String changedSize, @RequestParam String changedColor, @RequestParam String changedAmount,
                                    @RequestParam String originalColor, @RequestParam String originalSize, HttpServletResponse response,
                                    @RequestParam String itemNo, HttpServletRequest request) {

        // 기존에 있던 쿠키 삭제
        Cookie[] cookies = request.getCookies();
        String encodedOriginalColor = originalColor;

        try {
            encodedOriginalColor = URLEncoder.encode(originalColor, "UTF-8");
        }catch (Exception e) {
            e.printStackTrace();
            return "<script>alert('수정 중에 오류가 발생했습니다.\\n다시 수정해주세요.');location.href='/order';</script>";
        }

        if(cookies!=null){
            for(Cookie c : cookies){
                String name = c.getName();
                String value = c.getValue();

                if (name.equals("item_idx."+ itemNo + "." + encodedOriginalColor + "." + originalSize)) {
                    Cookie cookie = new Cookie(name, value);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }

        // 쿠키 재생성
        Cookie cookie;
        String encodedChangedColor;

        try {
            encodedChangedColor = URLEncoder.encode(changedColor, "UTF-8");
            cookie = new Cookie("item_idx."+ itemNo + "." + encodedChangedColor + "." + changedSize, changedAmount);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*3);
            response.addCookie(cookie);;

        }catch (Exception e){
            e.printStackTrace();
            return "<script>alert('수정 중에 오류가 발생했습니다.\\n다시 수정해주세요.');location.href='/order';</script>";
        }
        return "<script>location.href='/order';</script>";

    }

    final private PasswordEncoder passwordEncoder;
    final private MemberRepository memberRepository;

    @PostMapping("/order/payAction")
    @ResponseBody
    public String orderPayAction(OrderContentSaveRequestDto orderContentSaveRequestDto, HttpServletRequest request,
                                 @AuthenticationPrincipal User user, HttpServletResponse response,
                                 @RequestParam String[] colorList, @RequestParam String[] sizeList,
                                 @RequestParam String[] amountList, @RequestParam String[] itemCodeList,
                                 @Valid MemberJoinDto memberJoinDto, BindingResult bindingResult) {

        ////////////////////////////////////// member DB에 넣기 (회원가입) /////////////////////////////////
        String memberId = null;

        if (memberJoinDto.getUsername() != "") {

            LocalDate today = LocalDate.now();
            memberJoinDto.setMemberJoinDatetime(today);

            if (bindingResult.hasErrors()) {
                String detail = bindingResult.getFieldError().getDefaultMessage();
                String bindResultCode = bindingResult.getFieldError().getCode();
                System.out.println(detail + ":" + bindResultCode);
                return "<script>alert('" + detail + "'); history.back();</script>";
            }

            String encodedPassword = passwordEncoder.encode(memberJoinDto.getPassword());
            memberJoinDto.setPassword(encodedPassword);
            try {
                MemberEntity enity = memberJoinDto.toSaveEntity();
                memberRepository.save(enity);
            } catch (DataIntegrityViolationException e) {
                e.printStackTrace();
                bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
                return "<script>alert('이미 등록된 사용자입니다.');history.back();</script>";
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return "<script>alert('회원가입 실패했습니다.');history.back();</script>";
            }

            memberId = memberJoinDto.getUsername();
        }

        if (user != null) {
            memberId = user.getUsername();
        }

        ////////////////////////////////////// cart DB에 넣기 ////////////////////////////////////////////
        String[] cartCodeList = {null, null, null, null, null};
        Long orderCode = null;

        // orderNo
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderCode1 = format.format(new Date());
        String orderCode2 = String.format("%04d", (long) (Math.random() * 10000));
        orderCode = Long.parseLong(orderCode1 + orderCode2);

        for (int i=0; i<itemCodeList.length; i++) {

            // cartItemAmount
            Long cartItemAmount = Long.parseLong(amountList[i]);

            // itemOptionColor
            String itemOptionColor = colorList[i];

            // itemOptionSize
            String itemOptionSize = sizeList[i];

            // itemCode
            String itemCode = itemCodeList[i];
            ProductResponseDto productResponseDto = productService.findById(Long.parseLong(itemCode));

            // cartCode
            String cartCode = UUID.randomUUID().toString();
            cartCodeList[i] = cartCode;



            // cartDiscountPrice, cartItemPrice
            Long cartDiscountPrice = productResponseDto.getItemPrice() * productResponseDto.getItemDiscountRate() / 100;
            Long cartItemPrice = (productResponseDto.getItemPrice() - cartDiscountPrice) / 100 * 100;
            cartDiscountPrice = productResponseDto.getItemPrice() - cartItemPrice;

            CartSaveRequestDto cartSaveRequestDto = CartSaveRequestDto.builder()
                    .cartCode(cartCode)
                    .orderCode(orderCode)
                    .memberId(memberId)
                    .itemCode(itemCode)
                    .itemName(productResponseDto.getItemName())
                    .itemOptionColor(itemOptionColor)
                    .itemOptionSize(itemOptionSize)
                    .cartItemAmount(cartItemAmount)
                    .cartItemOriginalPrice(productResponseDto.getItemPrice())
                    .cartDiscountPrice(cartDiscountPrice)
                    .cartItemPrice(cartItemPrice)
                    .cartDate(LocalDateTime.now())
                    .build();

            // DB에 넣기
            boolean success = service2.save(cartSaveRequestDto);
            if (success) {
                try {
                    // 기존에 있던 쿠키 삭제하기
                    String encodedColor = URLEncoder.encode(itemOptionColor, "UTF-8");
                    Cookie cookie = new Cookie("item_idx." + itemCode +"." + encodedColor + "." + itemOptionSize, String.valueOf(cartItemAmount));
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }catch (Exception e){
                    e.printStackTrace();
                    return "<script>alert('결제 중 오류가 발생했습니다.\\n다시 결제해주세요.');location.href='/order';</script>";
                }
            } else {
                return "<script>alert('결제 중 오류가 발생했습니다.\\n다시 결제해주세요.');location.href='/order';</script>";
            }

        }



        ////////////////////////////////////// order DB에 넣기 ////////////////////////////////////////////

        // orderDTO 세팅하기
        orderContentSaveRequestDto.setOrderCode(orderCode);
        orderContentSaveRequestDto.setCartCode1(cartCodeList[0]);
        orderContentSaveRequestDto.setCartCode2(cartCodeList[1]);
        orderContentSaveRequestDto.setCartCode3(cartCodeList[2]);
        orderContentSaveRequestDto.setCartCode4(cartCodeList[3]);
        orderContentSaveRequestDto.setCartCode5(cartCodeList[4]);
        orderContentSaveRequestDto.setOrderDatetime(LocalDateTime.now());
        orderContentSaveRequestDto.setMemberId(memberId);
        orderContentSaveRequestDto.setMemberMileage(0L);
        orderContentSaveRequestDto.setMemberCoupon("0");
        if (orderContentSaveRequestDto.getOrderPayType().contains("휴대폰결제") ||
                orderContentSaveRequestDto.getOrderPayType().contains("삼성페이")) {
            orderContentSaveRequestDto.setOrderState("배송대기");
        }else{
            orderContentSaveRequestDto.setOrderState("결제대기");
        }

        boolean success = service2.saveOrder(orderContentSaveRequestDto);
        if (!success) {
            return "<script>alert('결제 중 오류가 발생했습니다.\\n다시 결제해주세요.');location.href='/order';</script>";
        }

        return "<script>location.href='/order/complete?orderCode=" + orderCode +"';</script>";
    }

    @GetMapping("/order/complete")
    public String orderComplete(@RequestParam Long orderCode, Model model){

        OrderResponseDto dto = service2.findByOrderCode(orderCode);
        model.addAttribute("order", dto);

        return "/user/order/order-complete";
    }

    // '/order' 끝 -----------------------------------------------------------------------------------------------
    // 이용약관 끝 -----------------------------------------------------------------------------------------------
    @GetMapping("/terms/terms/service")
    public String termsTermsService() {

        return "/user/popup/pop-page1";
    }

    @GetMapping("/terms/terms/privacy")
    public String termsTermsPrivacy() {

        return "/user/popup/pop-page2";
    }

    @GetMapping("/terms/terms/service/order")
    public String termsTermsServiceOrder() {

        return "/user/popup/pop-page3";
    }

    @GetMapping("/terms/terms/policy/order")
    public String termsTermsPolicyOrder() {

        return "/user/popup/pop-page4";
    }

    @PostMapping("/inquiry/test1")
    @ResponseBody
    public String test1(@RequestParam Long inquiryNo) {
        return ""+inquiryNo;
    }

    @PostMapping("/qna/test1")
    @ResponseBody
    public String test2(@RequestParam Long qnaId) {
        return ""+qnaId;
    }

}
