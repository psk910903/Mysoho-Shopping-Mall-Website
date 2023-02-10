package com.study.springboot.service;

import com.study.springboot.entity.CartEntity;
import com.study.springboot.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    final CartRepository cartRepository;

//    @Transactional(readOnly = true)
//    public CartResponseDto findByCart(String cartCode1) {
    public CartEntity findByCart(String cartCode1) {
        CartEntity entity = cartRepository.findByCartCodeNativeQuery(cartCode1);
        return entity;
    }
}
