package com.study.springboot.service;

import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.entity.ProductEntity;
import com.study.springboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Service1 {
    final ProductRepository productRepository;
    final ProductService productService;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByItem(int num) {
        List<ProductEntity> Limit= productRepository.findLimit(num);
        List<ProductResponseDto> list = Limit.stream().map(ProductResponseDto::new).collect(Collectors.toList());
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
}
