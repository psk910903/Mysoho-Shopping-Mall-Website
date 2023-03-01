package com.study.springboot.controller;

import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.order.OrderContentSaveRequestDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.order.OrderSearchDto;
import com.study.springboot.dto.product.FileResponse;
import com.study.springboot.dto.product.ProductResponseDto;

import com.study.springboot.dto.product.ProductSaveRequestDto;
import com.study.springboot.repository.OrderRepository;
import com.study.springboot.repository.ProductRepository;
import com.study.springboot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
  @RequestMapping("/admin/product/modify/action")
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
  @RequestMapping("/admin/product/registration/action")
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

  @PostMapping("/admin/imgUpload")
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
    Long totalPrice = service1.getTotalPrice(cartList);
    Long totalCount = service1.getTotalCount(cartList);

    dto.setOrderTotalPrice(totalPrice);
    dto.setOrderTotalCount(totalCount);
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

    model.addAttribute("bestItem", bestItem);
    model.addAttribute("list", list);

    return "/user/category/home";
  };

  //상품검색
  @GetMapping("/search")
  public String search(Model model,
                          @RequestParam(value = "keyword", required = false) String keyword) {
      List<ProductResponseDto> list = service1.findByKeyword(keyword);
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
  public String productContent(Model model,@PathVariable(value = "itemNo") Long itemNo) {
    ProductResponseDto dto = productService.findById(itemNo);

    model.addAttribute("dto", dto);
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

    for (int i = 0; i < orderList.size(); i++) {
      OrderResponseDto orderDto = orderList.get(i);
      String orderState = orderDto.getOrderState();
      if (orderState.equals("결제대기")) {
        stateType1++;
      } else if (orderState.equals("배송대기")) {
        stateType2++;
      }else if (orderState.equals("배송중")) {
        stateType3++;
      } else if (orderState.equals("배송완료")) {
        stateType4++;
      } else { //취소/반품
        stateType5++;
      }
      //비회원 주문번호에서 카트정보 가져오기
      cartList = service1.getCartListNonMember(orderDto);
      cartListModel.add(cartList);
      Long originalPrice =0L;
      Long discountPrice =0L;
      Long itemPrice =0L;

      for (int j = 0; j < cartList.size(); j++) {

        if (Objects.equals(cartList.get(j).getOrderNo(), orderDto.getOrderNo())) {
          originalPrice += cartList.get(j).getCartItemOriginalPrice();
          discountPrice += cartList.get(j).getCartDiscountPrice();
          itemPrice += cartList.get(j).getCartItemPrice();
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
}



