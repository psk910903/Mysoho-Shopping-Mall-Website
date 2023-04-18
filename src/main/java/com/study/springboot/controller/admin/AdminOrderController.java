package com.study.springboot.controller.admin;

import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.order.OrderContentSaveRequestDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.entity.repository.OrderRepository;
import com.study.springboot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class AdminOrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final NoticeService noticeService;

    @GetMapping("/admin/order")
    public String orderHome(){
        return "redirect:/admin/order/list?page=0";
    }

    // 관리자 주문 목록
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
        pageList = noticeService.getPageList(totalPage, page);
        model.addAttribute("pageList", pageList);
        model.addAttribute("list", list);
        model.addAttribute("findBy", findBy);
        model.addAttribute("keyword", keyword);
        model.addAttribute("dateStart", dateStart);
        model.addAttribute("dateEnd", dateEnd);
        //검색 상품 개수
        long listCount = orderRepository.count();
        model.addAttribute("listCount", listCount);
        return "admin/order/list";
    }

    //주문 정보 단건 조회
    @GetMapping("/admin/order/content")
    public String orderContent(@RequestParam(value = "orderNo") long id, Model model) {

        //주문정보
        OrderResponseDto dto = orderService.findById(id);
        model.addAttribute("dto", dto);
        List<CartResponseDto> cartList = cartService.getCartList(dto);

        model.addAttribute("cartList", cartList);
        model.addAttribute("dto", dto);

        return "admin/order/content";
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
}
