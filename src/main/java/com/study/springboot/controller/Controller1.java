package com.study.springboot.controller;

import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.dto.member.MemberResponseDto;
import com.study.springboot.dto.order.OrderContentSaveRequestDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.order.OrderSearchDto;
import com.study.springboot.dto.product.FileResponse;
import com.study.springboot.dto.product.ProductResponseDto;

import com.study.springboot.dto.product.ProductSaveRequestDto;
import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.entity.MemberEntity;
import com.study.springboot.repository.CartRepository;
import com.study.springboot.repository.OrderRepository;
import com.study.springboot.repository.ProductRepository;
import com.study.springboot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;

import java.util.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class Controller1 {
  final ProductService productService;
  final OrderService orderService;
  final CartService cartService;
  final ProductRepository productRepository;
  final OrderRepository orderRepository;
  final AwsS3Service awsS3Service;
  final Service1 service1;
  final Service2 service2;
  final Service3 service3;
  final CartRepository cartRepository;
  final ReviewService reviewService;
  final Service5 service5;

  private final Service6 service6;    // 경빈 Service6

  @GetMapping("/admin/product")
  public String productHome(){
    return "redirect:/admin/product/list?page=0";
  }

  //상품리스트
  @GetMapping("/admin/product/list")
  public String productList(Model model,
                            @RequestParam(value = "findByType1", required = false) String findByType1,
                            @RequestParam(value = "findByType2", required = false) String findByType2,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "page", defaultValue = "0") int page) {

    Page<ProductResponseDto> list;
    int totalPage;
    List<Integer> pageList;

    if ((findByType1 == null) && (findByType2 == null) && (keyword == null)
            || (findByType1.equals("null")) && (findByType2.equals("null")) && (keyword.equals("null"))
            || (findByType1.equals("all")) && (findByType2.equals("all")) && (keyword.equals(""))) {
      list = productService.findAll(page);
    } else {
      list = productService.findByKeyword(findByType1, findByType2, keyword, page);
    }
    totalPage = list.getTotalPages();
    pageList = productService.getPageList(totalPage, page);
    model.addAttribute("list", list);
    model.addAttribute("findByType1", findByType1);
    model.addAttribute("findByType2", findByType2);
    model.addAttribute("keyword", keyword);
    model.addAttribute("totalPage", totalPage);
    model.addAttribute("pageList", pageList);
    //상품 개수
    long listCount = productRepository.count();
    model.addAttribute("listCount", listCount);

    return "/admin/product/list";
  }

  //상품등록 폼
  @GetMapping("/admin/product/registration")
  public String productRegistration() {
    return "/admin/product/registration";
  }


  //상품 단건조회
  //http://localhost:8080/admin/product/content?item_no=1
  @GetMapping("/admin/product/content")
  public String productContent(@RequestParam(value = "itemNo") long id, Model model) {
    ProductResponseDto dto = productService.findById(id);
    model.addAttribute("dto", dto);

    return "admin/product/modify";
  }

  //상품삭제
  //http://localhost:8080/admin/product/delete?item_no=20000
  @ResponseBody
  @GetMapping("/admin/product/delete")
  public String productDelete(@RequestParam(value = "itemNo") long id) {
    boolean result = productService.productDelete(id);
    if (!result) {
      return "<script>alert('삭제 실패');location.href='/admin/product/list/';</script>";
    }
    return "<script>alert('삭제 완료');location.href='/admin/product/list/';</script>";
  }

  //상품 선택삭제
  @ResponseBody
  @RequestMapping("/admin/product/delete/check")
  public String delete (@RequestParam("itemNo") String arrStr){
    boolean result = productService.productDeleteCheck(arrStr);
    if (!result) {
      return "<script>alert('선택삭제 실패');location.href='/admin/product/list/';</script>";
    }
    return "<script>alert('선택삭제 완료');location.href='/admin/product/list/';</script>";
  }


  //상품수정
  @ResponseBody
  @RequestMapping("/find/admin/product/modify/action")
  public String productModify(@RequestParam MultipartFile uploadfile, ProductSaveRequestDto dto) {
    String url;
    if (uploadfile.getOriginalFilename().equals("")) {
      url = productService.findByUrl(dto.getItemNo());
    } else {
      url = awsS3Service.upload(uploadfile);
      new ResponseEntity<>(FileResponse.builder().
              uploaded(true).
              url(url).
              build(), HttpStatus.OK);
    }
    dto.setItemImageUrl(url);

    boolean result = productService.productModify(dto);
    if (!result) {
      return "<script>alert('수정 실패');location.href='/admin/product/list/';</script>";
    }
    return "<script>alert('수정 완료');location.href='/admin/product/list/';</script>";
  }

  //리스트에서 수정
  @ResponseBody
  @RequestMapping("/admin/product/list/modify/action")
  public String productModify(ProductSaveRequestDto dto) {

    boolean result = productService.productModify(dto);
    if (!result) {
      return "<script>alert('수정 실패');location.href='/admin/product/list/';</script>";
    }
    return "<script>alert('수정 완료');location.href='/admin/product/list/';</script>";
  }


  //상품등록
  @ResponseBody
  @RequestMapping("/find/admin/product/registration/action")
  public String productRegistrationAction(@RequestParam MultipartFile uploadfile, ProductSaveRequestDto dto)
          throws IllegalStateException, IOException {

      String url = awsS3Service.upload(uploadfile);

      new ResponseEntity<>(FileResponse.builder().
              uploaded(true).
              url(url).
              build(), HttpStatus.OK);

      dto.setItemImageUrl(url);

      boolean result = productService.productRegistration(dto);
      if (!result) {
        return "<script>alert('등록 실패');location.href='/admin/product/list/';</script>";
      }
      return "<script>alert('등록 완료');location.href='/admin/product/list/';</script>";

  }

  @PostMapping("find/admin/imgUpload")
  @ResponseBody
  public ResponseEntity<FileResponse> imgUpload(
          @RequestPart(value = "upload", required = false) MultipartFile fileload) throws Exception {

    return new ResponseEntity<>(FileResponse.builder().
            uploaded(true).
            url(productService.upload(fileload)).
            build(), HttpStatus.OK);
  }


  // 주문-----------------------------------------------------------------------------

  @GetMapping("/admin/order")
  public String orderHome(){
    return "redirect:/admin/order/list?page=0";
  }

  @GetMapping("/admin/order/list")
  public String orderList(Model model,
                          @RequestParam(value = "findBy", required = false) String findBy,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "dateStart", required = false) String dateStart,
                          @RequestParam(value = "dateEnd", required = false) String dateEnd,
                          @RequestParam(value = "page", defaultValue = "0") int page) throws ParseException {

    Page<OrderResponseDto> list;
    int totalPage;
    List<Integer> pageList;
    if ((findBy == null) && (keyword == null) && (dateStart == null) && (dateEnd == null)
            || (dateStart.equals("null")) && (dateEnd.equals("null")) && (keyword.equals("null"))
            || (dateStart.equals("")) && (dateEnd.equals("")) && (keyword.equals(""))) {
      list = orderService.findAll(page);
    } else {
      //오늘, 어제, 1주일, 1개월 검색
      if ((!dateStart.equals("")) && (dateEnd.equals(""))) {
        list = orderService.findByDate(dateStart, page);

        //기간 검색
      } else if ((!dateStart.equals("")) && (!dateEnd.equals(""))) {
        list = orderService.findByDate(dateStart, dateEnd, page);
      } else {

        //검색 키워드가 있을 때
        list = orderService.findByKeyword(findBy, keyword, page);
      }
    }
    totalPage = list.getTotalPages();
    pageList = orderService.getPageList(totalPage, page);
    model.addAttribute("pageList", pageList);
    model.addAttribute("list", list);
    model.addAttribute("findBy", findBy);
    model.addAttribute("keyword", keyword);
    model.addAttribute("dateStart", dateStart);
    model.addAttribute("dateEnd", dateEnd);
    //검색 상품 개수
    long listCount = orderRepository.count();
    model.addAttribute("listCount", listCount);
    return "/admin/order/list";
  }

  //주문 정보 단건 조회
  @GetMapping("/admin/order/content")
  public String orderContent(@RequestParam(value = "orderNo") long id, Model model) {

    //주문정보
    OrderResponseDto dto = orderService.findById(id);
    model.addAttribute("dto", dto);
    List<CartResponseDto> cartList = service1.getCartList(dto);

    model.addAttribute("cartList", cartList);
    model.addAttribute("dto", dto);

    return "/admin/order/content";
  }

  //리스트페이지에서 주문상태 변경
  @ResponseBody
  @RequestMapping("/admin/order/status/modify")
  public String orderStatusModify(OrderContentSaveRequestDto dto) {
    boolean result = orderService.statusModify(dto.getOrderNo(), dto.getOrderState());
    if (!result) {
      return "<script>alert('주문상태 변경 실패');location.href='/admin/order/list/';</script>";
    }
    return "<script>alert('주문상태 변경 완료');location.href='/admin/order/list/';</script>";
  }

  //단건 주문정보 수정
  @ResponseBody
  @RequestMapping("/admin/order/content/action")
  public String orderContentAction(OrderContentSaveRequestDto dto) {

    boolean result = orderService.updateOrderContent(dto);
    if (!result) {
      return "<script>alert('수정 실패');location.href='/admin/order/list/';</script>";
    }
    return "<script>alert('수정 완료');location.href='/admin/order/list/';</script>";
  }

  // 사용자화면 홈-----------------------------------------------------------------------------

  //홈페이지
  @GetMapping("/")
  public String mySoho(Model model) {
    List<ProductResponseDto> bestItem = service1.findByItem(6);
    List<ProductResponseDto> list = service1.findByItem(9);

    List<ProductResponseDto> sellCount = service1.SortItem(list, "판매량");
    List<ProductResponseDto> lowPrice = service1.SortItem(list, "낮은가격");
    List<ProductResponseDto> HighReview = service1.SortItem(list, "리뷰");
    List<ProductResponseDto> HighGrade = service1.SortItem(list, "평점");
    model.addAttribute("sellCount", sellCount);
    model.addAttribute("lowPrice", lowPrice);
    model.addAttribute("HighReview", HighReview);
    model.addAttribute("HighGrade", HighGrade);
    model.addAttribute("bestItem", bestItem);
    model.addAttribute("list", list);
    model.addAttribute("latestNotice", service1.findLatestNotice());


    return "/user/category/home";
  }

  //상품검색
  @GetMapping("/search")
  public String search(Model model,
                          @RequestParam(value = "keyword", required = false) String keyword) {
    List<ProductResponseDto> list = service1.findByKeyword(keyword);

      List<ProductResponseDto> sellCount = service1.SortItem(list, "판매량");
      List<ProductResponseDto> lowPrice = service1.SortItem(list, "낮은가격");
      List<ProductResponseDto> HighReview = service1.SortItem(list, "리뷰");
      List<ProductResponseDto> HighGrade = service1.SortItem(list, "평점");
      model.addAttribute("sellCount", sellCount);
      model.addAttribute("lowPrice", lowPrice);
      model.addAttribute("HighReview", HighReview);
      model.addAttribute("HighGrade", HighGrade);



      int count = list.size();
      model.addAttribute("list", list);
      model.addAttribute("keyword", keyword);
      model.addAttribute("count", count);
    return "/user/category/search";
  }

  // 카테고리-----------------------------------------------------------------------------

  @GetMapping("/plan/item/{category}")
  public String planItem(Model model,@PathVariable(value = "category") String category) {
    List<ProductResponseDto> list = service1.findByCategory(category);

    model.addAttribute("list", list);
    model.addAttribute("category", category);
    return "/user/category/content";
  }

  //상품상세-----------------------------------------------------------------------------
  @GetMapping("/product/{itemNo}")
  public String productContent(Model model,@PathVariable(value = "itemNo") Long itemNo,  @AuthenticationPrincipal User user) {
    ProductResponseDto dto = productService.findById(itemNo);
    String[] colorList = dto.getItemOptionColor().split(",");
    String[] sizeList = dto.getItemOptionSize().split(",");
    int colorCount = colorList.length;
    int sizeCount = sizeList.length;

    // member
    String memberId=null;
    MemberResponseDto memberResponseDto = null;
    if (user != null) {
      memberId = user.getUsername();
      memberResponseDto = service2.findByMemberIdMember(memberId);
    }

    // 이준하
    model.addAttribute("LoginMemberId", memberId);

    List<InquiryResponseDto> list2 = service5.findByItemNoList(itemNo);
    int listSize = list2.size();
    // 마스킹 처리
    List<String> nameList = new ArrayList<>();
    for(int i=0 ; i < list2.size();i++){

      String qnaName = list2.get(i).getMemberId();
      if(qnaName == null){
        qnaName = list2.get(i).getInquiryNickname();
      }
      String qnaHiddenName;
      if (qnaName.length() == 2){
        qnaHiddenName = qnaName.replace(qnaName.charAt(1), '*');
      }else if(qnaName.length() == 1){
        qnaHiddenName = qnaName;
      }
      else{
        qnaHiddenName = qnaName.substring(0,2);;
        //
        for (int j=0; j<qnaName.length()-2; j++){
          qnaHiddenName += "*";
        }
      }
      nameList.add(qnaHiddenName);
    }
    // 마스킹 처리 끝
  // 답변카운트 불러오기
    List<Long> inReplyCount = new ArrayList<>();
    for(int i =0; i< list2.size(); i++){
      Long CommentCount = service5.countByInquiryNo(list2.get(i).getInquiryNo());
      inReplyCount.add(CommentCount);
    }
  //답변카운트 불러오기끝
    model.addAttribute("namelist",nameList);
    model.addAttribute("inquiry",list2);
    model.addAttribute("listSize",listSize);
    model.addAttribute("inReplyCount", inReplyCount);

    // 이준하 끝
    //<경빈
    List<ReviewResponseDto> reviewResponseDtos = service6.findByReview(String.valueOf(itemNo)); // 2. 리뷰데이터

    int size = reviewResponseDtos.size();  // 3. 상품 리뷰 갯수

    byte sum = 0;       // 4. 상품별점 평균
    for(int i=0; i<size; i++){
      byte reviewStar = reviewResponseDtos.get(i).getReviewStar();
      sum += reviewStar;
    }
    double avg1 = sum / Double.valueOf(size);
    double avg2 = Math.round(avg1*10);
    double avg3 = avg2 / 10;

    List<ReviewResponseDto> reviewResponseDtos2 = service6.findByImgReview(String.valueOf(itemNo)); //5. 사진리뷰 데이터
    int size2 = reviewResponseDtos2.size(); //6. 사진리뷰 갯수

    model.addAttribute("dto", dto); // ->1
    model.addAttribute("list", reviewResponseDtos); // ->2
    model.addAttribute("listCount", size);  // ->3
    model.addAttribute("avgStar", avg3);    // ->4
    model.addAttribute("listImg",reviewResponseDtos2);  // ->5
    model.addAttribute("listImgCount", size2);  // ->6
    //경빈>


    model.addAttribute("member", memberResponseDto);

    model.addAttribute("colorCount", colorCount);
    model.addAttribute("sizeCount", sizeCount);
    model.addAttribute("colorList", colorList);
    model.addAttribute("sizeList", sizeList);
    model.addAttribute("dto", dto);
    model.addAttribute("cartList", null);
    return "/user/product/content";
  }

  //qna 작성 팝업 폼
  @GetMapping("/popup/qna-write")
  public String popupQnaWrite() {
    return "/user/popup/qna-write";
  }
  // 주문조회-----------------------------------------------------------------------------
  @GetMapping("/myorder")
  public String myorderList() {
    return "/user/user/myorder";
  }
//비회원
  @RequestMapping("/find/myorder/list")
  public String myorder(OrderSearchDto dto, Model model) {

    List<OrderResponseDto> orderList = service1.findByOrderNonMember(dto);
    List<CartResponseDto> cartList;
    List<List<CartResponseDto>> cartListModel = new ArrayList<>();

    int stateType1 = 0; //결제대기
    int stateType2 = 0; //배송대기
    int stateType3 = 0; //배송중
    int stateType4 = 0; //배송완료
    int stateType5 = 0; //취소/반품

    for (OrderResponseDto orderDto : orderList) {
      String orderState = orderDto.getOrderState();
      switch (orderState) {
        case "결제대기" -> stateType1++;
        case "배송대기" -> stateType2++;
        case "배송중" -> stateType3++;
        case "배송완료" -> stateType4++;
        default ->  stateType5++;//취소/반품
      }
      //비회원 주문번호에서 카트정보 가져오기
      cartList = service1.getCartListNonMember(orderDto);
      cartListModel.add(cartList);
      long originalPrice = 0L;
      long discountPrice = 0L;
      long itemPrice = 0L;

      for (CartResponseDto cartDto : cartList) {

        if (Objects.equals(cartDto.getOrderCode(), orderDto.getOrderCode())) {
          originalPrice += cartDto.getCartItemOriginalPrice() * cartDto.getCartItemAmount();
          discountPrice += cartDto.getCartDiscountPrice() * cartDto.getCartItemAmount();
          itemPrice += cartDto.getCartItemPrice() * cartDto.getCartItemAmount();
        }

      }
      orderDto.setOrderItemOriginalPrice(originalPrice); //(할인 전)상품가격
      orderDto.setOrderDiscountPrice(discountPrice);//할인율이 적용된 차감될 금액
      orderDto.setOrderItemPrice(itemPrice); // (할인 적용된 결제당시)상품가격
    }
    int cartCount = cartListModel.size();
    int orderCount = orderList.size();
    model.addAttribute("orderCount", orderCount);
    model.addAttribute("stateType1", stateType1);
    model.addAttribute("stateType2", stateType2);
    model.addAttribute("stateType3", stateType3);
    model.addAttribute("stateType4", stateType4);
    model.addAttribute("stateType5", stateType5);

    model.addAttribute("cartCount", cartCount);
    model.addAttribute("orderList", orderList);
    model.addAttribute("cartListModel", cartListModel);

    return "/user/user/myorder-list";
  }

  //마이페이지 홈 회원
  @RequestMapping("/myorder/lists")
  public String myInfo(@AuthenticationPrincipal User user,
                       HttpServletRequest request, Model model) {
    if (user == null) {
      System.out.println("no user");
    } else {
      String memberId = user.getUsername();
      MemberEntity entity = service3.findByUserId(memberId);
      request.getSession().setAttribute("username", entity.getMemberName());
      request.getSession().setAttribute("memberMileage", entity.getMemberMileage());
      request.getSession().setAttribute("memberCoupon", entity.getMemberCoupon());

      List<OrderResponseDto> orderList = cartService.findByOrderList(memberId);
      List<CartResponseDto> cartList;
      List<List<CartResponseDto>> cartListModel = new ArrayList<>();

      int stateType1 = 0;
      int stateType2 = 0;
      int stateType3 = 0;
      int stateType4 = 0;
      int stateType5 = 0;

      for (OrderResponseDto orderDto : orderList) {
        String orderState = orderDto.getOrderState();
        switch (orderState) {
          case "결제대기" -> stateType1++;
          case "배송대기" -> stateType2++;
          case "배송중" -> stateType3++;
          case "배송완료" -> stateType4++;
          default ->  stateType5++;//취소/반품
        }
        //비회원 주문번호에서 카트정보 가져오기
        cartList = service1.getCartListMember(orderDto);
        cartListModel.add(cartList);
        long originalPrice = 0L;
        long discountPrice = 0L;
        long itemPrice = 0L;

        for (CartResponseDto cartDto : cartList) {

          if (Objects.equals(cartDto.getOrderCode(), orderDto.getOrderCode())) {
            originalPrice += cartDto.getCartItemOriginalPrice() * cartDto.getCartItemAmount();
            discountPrice += cartDto.getCartDiscountPrice() * cartDto.getCartItemAmount();
            itemPrice += cartDto.getCartItemPrice() * cartDto.getCartItemAmount();
          }

        }
        orderDto.setOrderItemOriginalPrice(originalPrice); //(할인 전)상품가격
        orderDto.setOrderDiscountPrice(discountPrice);//할인율이 적용된 차감될 금액
        orderDto.setOrderItemPrice(itemPrice); // (할인 적용된 결제당시)상품가격
      }

      List<ReviewResponseDto> ReviewList = reviewService.findByMemberId(memberId); //사용자가 작성한 리뷰

      int cartCount = cartListModel.size();
      int orderCount = orderList.size();
      model.addAttribute("ReviewList", ReviewList);
      model.addAttribute("orderCount", orderCount);
      model.addAttribute("stateType1", stateType1);
      model.addAttribute("stateType2", stateType2);
      model.addAttribute("stateType3", stateType3);
      model.addAttribute("stateType4", stateType4);
      model.addAttribute("stateType5", stateType5);

      model.addAttribute("cartCount", cartCount);
      model.addAttribute("orderList", orderList);
      model.addAttribute("cartListModel", cartListModel);

    }
    return "/user/user/myorder-list-user";
  }
  // 상품 대표이지미 확대
  @RequestMapping("/enlarge/{itemNo}")
  public String enlarge(Model model,@PathVariable(value = "itemNo") Long itemNo){
    String itemImageUrl = productService.findById(itemNo).getItemImageUrl();
    model.addAttribute("itemImageUrl", itemImageUrl);
    return "/user/enlarge/enlargeProductImg";
  }

  // 상품 상세설명 확대
  @RequestMapping("/enlarge/content/{itemNo}")
  public String enlargeContent(Model model,@PathVariable(value = "itemNo") Long itemNo){
    String itemInfo = productService.findById(itemNo).getItemInfo();
    model.addAttribute("itemInfo", itemInfo);
    return "/user/enlarge/enlargeProductInfo";
  }
}



