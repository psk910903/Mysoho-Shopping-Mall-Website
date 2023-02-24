package com.study.springboot.service;

import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.order.OrderSearchDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.entity.CartEntity;
import com.study.springboot.entity.OrderEntity;
import com.study.springboot.entity.ProductEntity;
import com.study.springboot.repository.CartRepository;
import com.study.springboot.repository.OrderRepository;
import com.study.springboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Service1 {
    final ProductRepository productRepository;
    final ProductService productService;
    final OrderRepository orderRepository;
    final CartRepository cartRepository;
    final CartService cartService;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByItem(int num) {
        List<ProductEntity> entityList;
        if (num == 6) {
            entityList= productRepository.findLimit6();
        } else {
            entityList= productRepository.findLimit9();
        }

        List<ProductResponseDto> list = entityList.stream().map(ProductResponseDto::new).collect(Collectors.toList());
        return setItemDiscountPrice(list);
    }

    List<ProductResponseDto> setItemDiscountPrice( List<ProductResponseDto> list){
        for (int i = 0; i < list.size(); i++) {
            Long itemPrice = list.get(i).getItemPrice();
            Long itemDiscountRate = list.get(i).getItemDiscountRate();
            long price = (long) (Math.floor((itemPrice * ( (100 - itemDiscountRate) * 0.01))/100)) *100;
            list.get(i).setItemDiscountPrice(price);
        }
        return list;
    }


    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByKeyword(String keyword) {
        List<ProductEntity> list =productRepository.findByItemNameContaining(keyword);
        return list.stream().map(ProductResponseDto::new).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByCategory(String keyword) {
        List<ProductEntity> entityList;
        if ( keyword.equals("SALE") ) {
            entityList =productRepository.findByCategorySale(keyword);
        } else {
            entityList =productRepository.findByCategory(keyword);
        }
        List<ProductResponseDto> list = entityList.stream().map(ProductResponseDto::new).collect(Collectors.toList());
        return setItemDiscountPrice(list);
    }


    @Transactional(readOnly = true)
    public List<OrderResponseDto> findByOrder(OrderSearchDto dto) {
        String sender = dto.getSender();
        String phone = dto.getPhone1() + dto.getPhone2();

        List<OrderEntity> orderEntity = orderRepository.findByOrder(sender, phone);

        return orderEntity.stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }



    @Transactional(readOnly = true)
    public List<CartResponseDto> getCartList(OrderResponseDto dto) {
        List<CartResponseDto> cartList = new ArrayList<>();
        if (dto.getCartCode1() != null) {
            cartList.add(cartService.findByCart(dto.getCartCode1()));
        }
        if (dto.getCartCode2() != null) {
            cartList.add(cartService.findByCart(dto.getCartCode2()));
        }
        if (dto.getCartCode3() != null) {
            cartList.add(cartService.findByCart(dto.getCartCode3()));
        }
        if (dto.getCartCode4() != null) {
            cartList.add(cartService.findByCart(dto.getCartCode4()));
        }
        if (dto.getCartCode5() != null) {
            cartList.add(cartService.findByCart(dto.getCartCode5()));
        }

        return cartList;
    }


    @Transactional(readOnly = true)
    public Long getTotalPrice(List<CartResponseDto> cartList) {
        Long totalPrice = 0L;
        for (int i = 0; i < cartList.size(); i++) {
            totalPrice += cartList.get(i).getCartItemPrice();
        }
        return totalPrice;
    }

    @Transactional(readOnly = true)
    public Long getTotalCount(List<CartResponseDto> cartList) {
        Long totalCount = 0L;
        for (int i = 0; i < cartList.size(); i++) {
            totalCount += cartList.get(i).getCartItemAmount();
        }
        return totalCount;
    }

}
