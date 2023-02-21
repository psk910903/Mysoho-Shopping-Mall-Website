package com.study.springboot.service;

import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.entity.ProductEntity;
import com.study.springboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Service1 {
    final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findBestItem() {

        List<ProductEntity> list = productRepository.findLimit6();
        String itemName = list.get(0).getItemName();
        System.out.println("itemName = " + itemName);

        return list.stream().map(ProductResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findLimit9() {

        List<ProductEntity> list = productRepository.findLimit9();
        String itemName = list.get(0).getItemName();
        System.out.println("itemName = " + itemName);
        return list.stream().map(ProductResponseDto::new).collect(Collectors.toList());
    }
}
