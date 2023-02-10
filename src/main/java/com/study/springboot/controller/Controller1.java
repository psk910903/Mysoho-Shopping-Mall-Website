package com.study.springboot.controller;

<<<<<<< HEAD
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.entity.Product;
=======
import com.study.springboot.dto.order.OrderInfoSaveRequestDto;
import com.study.springboot.dto.order.OrderListResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;

import com.study.springboot.dto.product.ProductSaveRequestDto;
import com.study.springboot.dto.product.ProductSearchDto;
import com.study.springboot.entity.CartEntity;
import com.study.springboot.entity.OrderEntity;
import com.study.springboot.entity.ProductEntity;
import com.study.springboot.repository.OrderRepository;
import com.study.springboot.repository.ProductRepository;
import com.study.springboot.service.CartService;
import com.study.springboot.service.OrderService;
>>>>>>> sunkyo
import com.study.springboot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class Controller1 {
    final ProductService productService;
    final OrderService orderService;
    final CartService cartService;
    final ProductRepository productRepository;
    final OrderRepository orderRepository;

    //상품리스트
    @GetMapping("/admin/productList")
    public String productList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        //페이징처리
        Page<ProductEntity> paging = productService.getList(page);
        model.addAttribute("paging", paging);

        //페이지 5개로 고정
        List<Integer> pageList = productService.getPageList(page, paging);
        model.addAttribute("pageList", pageList);

        //페이징에서 dto 리스트 꺼내기
        List<ProductResponseDto> list = productService.getDtoList(paging);
        model.addAttribute("list", list);

        //검색 개수
        long listCount = productRepository.count();
        model.addAttribute("listCount", listCount);

        return "/admin/product/productList";
    }

<<<<<<< HEAD

=======
    //상품등록 폼
    @GetMapping("/admin/productRegistration")
    public String productRegistration() {
        return "/admin/product/productRegistration";
    }


    //상품 단건조회
    //http://localhost:8080/admin/productInfo?item_no=1
    @GetMapping("admin/productInfo")
    public String productInfo(@RequestParam(value = "item_no") long id, Model model) {
        ProductResponseDto dto = productService.findById(id);
        model.addAttribute("dto", dto);

        return "admin/product/productModify";
    }

    //상품삭제
    //http://localhost:8080/admin/productDelete?item_no=20000
    @ResponseBody
    @GetMapping("admin/productDelete")
    public String productDelete(@RequestParam(value = "item_no") long id) {
        boolean result = productService.productDelete(id);
        if (!result) {
            return "<script>alert('삭제 실패');location.href='/admin/productList/';</script>";
        }
        return "<script>alert('삭제 성공');location.href='/admin/productList/';</script>";
    }

    //상품수정
    @ResponseBody
    @RequestMapping("admin/productModify/action")
    public String productModify(ProductSaveRequestDto dto) {
        boolean result = productService.productModify(dto);
        if (!result) {
            return "<script>alert('수정 실패');location.href='/admin/productList/';</script>";
        }
        return "<script>alert('수정 성공');location.href='/admin/productList/';</script>";
    }

    //상품등록
    @ResponseBody
    @RequestMapping("admin/productRegistration/action")
    public String productModifyAction(ProductSaveRequestDto dto) {
        boolean result = productService.productRegistration(dto);
        if (!result) {
            return "<script>alert('등록 실패');location.href='/admin/productList/';</script>";
        }
        return "<script>alert('등록 성공');location.href='/admin/productList/';</script>";
    }
    // 상품 검색-----------------------------------------------------------------------------

//    @RequestMapping("/productSearch")
//    public String productSearch(ProductSearchDto dto) {
//        productService.productSearch(dto)
//    }
>>>>>>> sunkyo


    // 주문-----------------------------------------------------------------------------
    @GetMapping("/admin/orderList")
    public String orderList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        //페이징처리
        Page<OrderEntity> paging = orderService.getList(page);
        model.addAttribute("paging", paging);

        //페이지 5개로 고정
        List<Integer> pageList = orderService.getPageList(page, paging);
        model.addAttribute("pageList", pageList);

        //페이징에서 dto 리스트 꺼내기
        List<OrderListResponseDto> list = orderService.getDtoList(paging);
        model.addAttribute("list", list);

        //검색 상품 개수
        long listCount = orderRepository.count();
        model.addAttribute("listCount", listCount);

        return "/admin/order/orderList";
    }

    //주문 정보 단건 조회
    //http://localhost:8080/admin/orderInfo?order_no=10000
    @GetMapping("admin/orderInfo")
    public String orderInfo(@RequestParam(value = "order_no") long id, Model model) {

        //주문정보
        OrderListResponseDto dto = orderService.findById(id);
        model.addAttribute("dto", dto);

        //장바구니 정보
        String cartCode1 = dto.getCart_code_1();
        CartEntity cartEntity = cartService.findByCart(cartCode1);
        model.addAttribute("cartDto", cartEntity);

        return "/admin/order/orderInfo";
    }

    //리스트페이지에서 주문상태 변경
    @ResponseBody
    @RequestMapping("admin/orderStateUpdate")
    public String orderStateUpdate(OrderInfoSaveRequestDto dto) {
        boolean result = orderService.stateUpdate(dto.getOrder_no(), dto.getOrder_state());
        if (!result) {
            return "<script>alert('주문상태 수정 실패');location.href='/admin/orderList/';</script>";
        }
        return "<script>alert('주문상태 수정 성공');location.href='/admin/orderList/';</script>";
    }

    //단건 주문정보 수정
    @ResponseBody
    @RequestMapping("/orderInfoAction")
    public String orderInfoAction(OrderInfoSaveRequestDto dto) {
        boolean result = orderService.updateOrderInfo(dto);
        if (!result) {
            return "<script>alert('수정 실패');location.href='/admin/orderList/';</script>";
        }
        return "<script>alert('수정 성공');location.href='/admin/orderList/';</script>";
    }

}
