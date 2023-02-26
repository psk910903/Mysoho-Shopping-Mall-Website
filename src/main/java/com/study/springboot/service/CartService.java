package com.study.springboot.service;

import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.entity.CartEntity;
import com.study.springboot.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    final CartRepository cartRepository;

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
}
