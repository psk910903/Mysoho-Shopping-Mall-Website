package com.study.springboot.service;

import com.study.springboot.dto.order.OrderContentSaveRequestDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.order.OrderSearchDto;
import com.study.springboot.entity.*;
import com.study.springboot.entity.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    //리스트 페이징
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> findAll(int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("orderDatetime"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<OrderEntity> list = orderRepository.findAll(pageable);

        return list.map(OrderResponseDto::new);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findById(Long id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
        return new OrderResponseDto(entity);
    }

    @Transactional
    public boolean updateOrderContent(OrderContentSaveRequestDto dto) {
        try {
            OrderEntity entity = orderRepository.findById(dto.getOrderNo()).get();
            OrderResponseDto responseDto = new OrderResponseDto(entity);
            responseDto.setOrderRecipientName(dto.getOrderRecipientName());
            responseDto.setOrderRecipientPhone(dto.getOrderRecipientPhone());
            responseDto.setOrderRecipientAddrNumber(dto.getOrderRecipientAddrNumber());
            responseDto.setOrderRecipientAddr1(dto.getOrderRecipientAddr1());
            responseDto.setOrderRecipientAddr2(dto.getOrderRecipientAddr2());
            responseDto.setOrderState(dto.getOrderState());
            orderRepository.save(responseDto.toEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Transactional
    public boolean statusModify(Long id, String orderState) {

        try {
            OrderResponseDto dto = findById(id);
            dto.setOrderState(orderState);
            orderRepository.save(dto.toEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> findByKeyword(String findBy, String keyword, int page) {

        Page<OrderEntity> list;
        Pageable pageable = PageRequest.of(page, 10);

        if (findBy.equals("all")) {
            list = orderRepository.findByAllContaining(keyword, pageable);
        }else if (findBy.equals("orderName")){
            list = orderRepository.findByOrderNameContaining(keyword, pageable);
        }else if (findBy.equals("orderPhone")) {
            list = orderRepository.findByOrderPhoneContaining(keyword, pageable);
        }else if (findBy.equals("orderNo")) {
            list = orderRepository.findByOrderNoContaining(keyword, pageable);
        } else {
            //주문상태 검색
            list = orderRepository.findByOrderState(keyword, pageable);
        }

        return list.map(OrderResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> findByDate(String start, String end, int page) throws ParseException {

        Page<OrderEntity> list;
        Pageable pageable = PageRequest.of(page, 10);

        String[] date = dateSetting(start, end);
        String dateStartStr = date[0];
        String dateEndStr = date[1];

        list = orderRepository.findByOrderNoContaining(dateStartStr, dateEndStr, pageable);
        return list.map(OrderResponseDto::new);
    }


    public Page<OrderResponseDto> findByDate(String mode, int page) throws ParseException {
        String[] date = dateSetting(mode);
        String dateStartStr = date[0];
        String dateEndStr = date[1];
        return findByDate(dateStartStr, dateEndStr, page);
    }

    @Transactional(readOnly = true)
    public String[] dateSetting(String start, String end)throws ParseException {
        //문자열을 날짜형식으로 변환
        DateFormat sdFormatStart = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat sdFormatEnd = new SimpleDateFormat("yyyy-MM-dd");
        Date tempDateStart = sdFormatStart.parse(start);
        Date tempDateEnd = sdFormatEnd.parse(end);
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(tempDateStart);
        cal2.setTime(tempDateEnd);
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        cal2.add(Calendar.DATE, +1);

        //날짜형식을 문자열로 변환
        String dateStartStr = df1.format(cal1.getTime())+" 00:00:00";
        String dateEndStr = df2.format(cal2.getTime())+" 00:00:00";

        String[] date = {dateStartStr, dateEndStr};
        return date;
    }

    @Transactional(readOnly = true)
    public String[] dateSetting(String mode) {
        //오늘날짜로 date객체 2개 생성 (~부터 ~까지로 검색에 사용목적)
        Date tempDateStart = new Date();
        Date tempDateEnd = new Date();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(tempDateStart);
        cal2.setTime(tempDateEnd);
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

        //매개변수로 들어온 모드 확인 후 검색기간 수정
        if (mode.equals("today")) {
            cal2.add(Calendar.DATE, +1);
        } else if (mode.equals("yesterday")) {
            cal1.add(Calendar.DATE, -1);
            cal2.add(Calendar.DATE, +1);
        } else if (mode.equals("week")) {
            cal1.add(Calendar.DATE, -7);
            cal2.add(Calendar.DATE, +1);
        } else if (mode.equals("month")) {
            cal1.add(Calendar.MONTH, -1);
            cal2.add(Calendar.DATE, +1);
        }

        String dateStartStr = df1.format(cal1.getTime());
        String dateEndStr = df2.format(cal2.getTime());
        String[] date = {dateStartStr, dateEndStr};
        return date;
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> findByOrderNonMember(OrderSearchDto dto) {
        String sender = dto.getSender();
        String phone = dto.getPhone1() + dto.getPhone2();

        List<OrderEntity> orderEntity = orderRepository.findByOrderNonMember(sender, phone);

        return orderEntity.stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }

    //주문정보 db에 저장
    @Transactional
    public Boolean saveOrderDto(final OrderContentSaveRequestDto dto) { // 이름 나중에 바꾸기

        try{
            OrderEntity entity = dto.toEntity();
            orderRepository.save(entity);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findByOrderCode(Long orderCode){
        Optional<OrderEntity> entity = orderRepository.findByOrderCode(orderCode);
        if (!entity.isPresent()){
            return null;
        }
        return new OrderResponseDto(entity.get());
    }

    @Transactional(readOnly = true)
    public Long generateOrderNo() {
        // orderNo
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderCode1 = format.format(new Date());
        String orderCode2 = String.format("%04d", (long) (Math.random() * 10000));
        return Long.parseLong(orderCode1 + orderCode2);
    }

    @Transactional
    public OrderContentSaveRequestDto saveOrderDtoComplete(OrderContentSaveRequestDto orderSaveRequestDto,
                                                           Long orderCode, String[] cartCodeList, String memberId) {
        orderSaveRequestDto.setOrderCode(orderCode);
        orderSaveRequestDto.setCartCode1(cartCodeList[0]);
        orderSaveRequestDto.setCartCode2(cartCodeList[1]);
        orderSaveRequestDto.setCartCode3(cartCodeList[2]);
        orderSaveRequestDto.setCartCode4(cartCodeList[3]);
        orderSaveRequestDto.setCartCode5(cartCodeList[4]);
        orderSaveRequestDto.setOrderDatetime(LocalDateTime.now());
        orderSaveRequestDto.setMemberId(memberId);
        orderSaveRequestDto.setMemberMileage(0L);
        orderSaveRequestDto.setMemberCoupon("0");
        if (orderSaveRequestDto.getOrderPayType().contains("휴대폰결제") ||
                orderSaveRequestDto.getOrderPayType().contains("삼성페이")) {
            orderSaveRequestDto.setOrderState("배송대기");
        }else{
            orderSaveRequestDto.setOrderState("결제대기");
        }

        return orderSaveRequestDto;
    }


}
