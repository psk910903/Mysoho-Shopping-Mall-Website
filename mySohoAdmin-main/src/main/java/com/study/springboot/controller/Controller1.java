package com.study.springboot.controller;

import com.study.springboot.dto.order.OrderContentSaveRequestDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.product.FileResponse;
import com.study.springboot.dto.product.ProductResponseDto;

import com.study.springboot.dto.product.ProductSaveRequestDto;
import com.study.springboot.entity.CartEntity;
import com.study.springboot.entity.ProductEntity;
import com.study.springboot.repository.OrderRepository;
import com.study.springboot.repository.ProductRepository;
import com.study.springboot.service.AwsS3Service;
import com.study.springboot.service.CartService;
import com.study.springboot.service.OrderService;
import com.study.springboot.service.ProductService;
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

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class Controller1 {
  final ProductService productService;
  final OrderService orderService;
  final CartService cartService;
  final ProductRepository productRepository;
  final OrderRepository orderRepository;
  final AwsS3Service awsS3Service;

  @GetMapping("/product")
  public String productHome(){
    return "redirect:/admin/product/list?page=0";
  }

  //상품리스트
  @GetMapping("/product/list")
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
  @GetMapping("/product/registration")
  public String productRegistration() {
    return "/admin/product/registration";
  }


  //상품 단건조회
  //http://localhost:8080/admin/product/content?item_no=1
  @GetMapping("/product/content")
  public String productContent(@RequestParam(value = "itemNo") long id, Model model) {
    ProductResponseDto dto = productService.findById(id);
    model.addAttribute("dto", dto);

    return "admin/product/modify";
  }

  //상품삭제
  //http://localhost:8080/admin/product/delete?item_no=20000
  @ResponseBody
  @GetMapping("/product/delete")
  public String productDelete(@RequestParam(value = "itemNo") long id) {
    boolean result = productService.productDelete(id);
    if (!result) {
      return "<script>alert('삭제 실패');location.href='/admin/product/list/';</script>";
    }
    return "<script>alert('삭제 완료');location.href='/admin/product/list/';</script>";
  }

  //상품 선택삭제
  @ResponseBody
  @RequestMapping("/product/delete/check")
  public String delete (@RequestParam("itemNo") String arrStr){
    boolean result = productService.productDeleteCheck(arrStr);
    if (!result) {
      return "<script>alert('선택삭제 실패');location.href='/admin/product/list/';</script>";
    }
    return "<script>alert('선택삭제 완료');location.href='/admin/product/list/';</script>";
  }


  //상품수정
  @ResponseBody
  @RequestMapping("/product/modify/action")
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
  @RequestMapping("/product/list/modify/action")
  public String productModify(ProductSaveRequestDto dto) {

    boolean result = productService.productModify(dto);
    if (!result) {
      return "<script>alert('수정 실패');location.href='/admin/product/list/';</script>";
    }
    return "<script>alert('수정 완료');location.href='/admin/product/list/';</script>";
  }


  //상품등록
  @ResponseBody
  @RequestMapping("/product/registration/action")
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

  @PostMapping("/imgUpload")
  @ResponseBody
  public ResponseEntity<FileResponse> imgUpload(
          @RequestPart(value = "upload", required = false) MultipartFile fileload) throws Exception {

    return new ResponseEntity<>(FileResponse.builder().
            uploaded(true).
            url(productService.upload(fileload)).
            build(), HttpStatus.OK);
  }


  // 주문-----------------------------------------------------------------------------

  @GetMapping("/order")
  public String orderHome(){
    return "redirect:/admin/order/list?page=0";
  }

  @GetMapping("/order/list")
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
  @GetMapping("/order/content")
  public String orderContent(@RequestParam(value = "orderNo") long id, Model model) {

    //주문정보
    OrderResponseDto dto = orderService.findById(id);
    model.addAttribute("dto", dto);

    //장바구니 정보
    String cartCode1 = dto.getCartCode1();
    CartEntity cartEntity = cartService.findByCart(cartCode1);
    model.addAttribute("cartDto", cartEntity);

    return "/admin/order/content";
  }

  //리스트페이지에서 주문상태 변경
  @ResponseBody
  @RequestMapping("/order/status/modify")
  public String orderStatusModify(OrderContentSaveRequestDto dto) {
    boolean result = orderService.statusModify(dto.getOrderNo(), dto.getOrderState());
    if (!result) {
      return "<script>alert('주문상태 변경 실패');location.href='/admin/order/list/';</script>";
    }
    return "<script>alert('주문상태 변경 완료');location.href='/admin/order/list/';</script>";
  }

  //단건 주문정보 수정
  @ResponseBody
  @RequestMapping("/order/content/action")
  public String orderContentAction(OrderContentSaveRequestDto dto) {

    boolean result = orderService.updateOrderContent(dto);
    if (!result) {
      return "<script>alert('수정 실패');location.href='/admin/order/list/';</script>";
    }
    return "<script>alert('수정 완료');location.href='/admin/order/list/';</script>";
  }
}
