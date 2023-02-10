package com.study.springboot.service;

import com.study.springboot.dto.BoardResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.entity.Board;
import com.study.springboot.entity.product.Product;
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
public class ProductService {
    final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<Product> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("item_update_datetime")); //최신글을 먼저 보여준다.

        Pageable pageable = PageRequest.of(page, 5); //5개씩
        return productRepository.findAll(pageable);
    }

    public List<ProductResponseDto> findAll() {
        //정렬기능 추가
        Sort sort = Sort.by(Sort.Direction.DESC, "item_no", "item_update_datetime");
        List<Product> list = productRepository.findAll();
        System.out.println(list.size());

        //List<Board>를 List<BoardResponseDto>로 변환
        return list.stream().map(ProductResponseDto::new).collect(Collectors.toList());
    }

}
