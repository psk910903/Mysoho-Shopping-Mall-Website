package com.study.springboot.service;

import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.entity.CartEntity;
import com.study.springboot.entity.OrderEntity;
import com.study.springboot.repository.CartRepository;
import com.study.springboot.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    final CartRepository cartRepository;
    final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public CartResponseDto findByCart(String cartCode) {
        CartEntity entity = cartRepository.findByCart(cartCode);
        return new CartResponseDto(entity);
    }
    @Transactional(readOnly = true)
    public CartResponseDto findByCartNonMember(String cartCode) {
        CartEntity entity = cartRepository.findByCartNonMember(cartCode);
        return new CartResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public CartResponseDto findByCartMember(String cartCode) {
        CartEntity entity = cartRepository.findByCartMember(cartCode);
        return new CartResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> findByOrderList(String id) {
        //회원 아이디로 카트테이블에서 정보 가져오기
        List<CartEntity> cartEntityList = cartRepository.findByCartMemberId(id);
        //entity -> dto로 변환
        List<CartResponseDto> cartDtoList = cartEntityList.stream().map(CartResponseDto::new).toList();
        //카트 테이블에서 주문번호만 추출해서 담을 orderCodeList 생성
        List<Long> orderCodeList = new ArrayList<>();
        //반복해서 주문번호만 추출 후 리스트에 담기
        for (CartResponseDto cartDto : cartDtoList) {
            orderCodeList.add(cartDto.getOrderCode());
        }
        //주문번호리스트에서 중복 값 제거
        List<Long> newList = orderCodeList.stream().distinct().toList();
        //주문번호들로 주문테이블에서 정보 가져와서 담을 리스트 생성
        List<OrderResponseDto> orderList = new ArrayList<>();
        //반복해서 주문테이블 객체 가져와서 dto로 변환 후 리스트에 담기
        for (Long orderCode : newList) {
            OrderEntity orderEntity = orderRepository.findByOrderCode(orderCode).get();
            OrderResponseDto dto = new OrderResponseDto(orderEntity);
            orderList.add(dto);
        }
        return orderList;
    }
}
