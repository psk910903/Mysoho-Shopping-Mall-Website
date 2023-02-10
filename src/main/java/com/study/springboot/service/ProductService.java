package com.study.springboot.service;


import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.product.ProductSaveRequestDto;
import com.study.springboot.dto.product.ProductSearchDto;
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
public class ProductService {
    final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductEntity> getList(int page) {
//        List<Sort.Order> sorts = new ArrayList<>();
//        sorts.add(Sort.Order.desc("item_no")); //최신글을 먼저 보여준다. 주석 풀면 오류 발생

        Pageable pageable = PageRequest.of(page, 2); //2개씩
        return productRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Integer> getPageList(int page, Page<ProductEntity> paging) {
        int totalPage = paging.getTotalPages();
        List<Integer> pageList = new ArrayList<>();
        if (totalPage <= 5){
            for (Integer i=0; i<=totalPage-1; i++){
                pageList.add(i);
            }
            return pageList;
        }else if(page >= 0 && page <= 2){
            for (Integer i=0; i<=4; i++){
                pageList.add(i);
            }
            return pageList;
        }
        else if (page >= totalPage-3 && page <= totalPage-1){
            for (Integer i=5; i>=1; i--){
                pageList.add(totalPage - i);
            }
            return pageList;
        }else{
            for (Integer i=-2; i<=2; i++){
                pageList.add(page + i);
            }
            return pageList;
        }
    }
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getDtoList(Page<ProductEntity> paging){
        List<ProductResponseDto> list = new ArrayList<>();
        for (ProductEntity entity : paging) {
            list.add(new ProductResponseDto(entity));
        }
        return list;
    }


    public List<ProductResponseDto> findAll() {
        //정렬기능 추가
        Sort sort = Sort.by(Sort.Direction.DESC, "item_no", "item_update_datetime");
        List<ProductEntity> list = productRepository.findAll();
        System.out.println(list.size());

        //List<Board>를 List<BoardResponseDto>로 변환
        return list.stream().map(ProductResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findById(Long id) {

        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
        return new ProductResponseDto(entity);

    }

    //삭제
    @Transactional
    public boolean productDelete(Long id){
        try {
            ProductEntity entity = productRepository.findById(id).get();
            productRepository.delete(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //수정
    @Transactional
    public boolean productModify(ProductSaveRequestDto dto){
        try {
            ProductEntity entity = dto.toUpdateEntity();
            productRepository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //등록
    @Transactional
    public boolean productRegistration(ProductSaveRequestDto dto){
        try {
            ProductEntity entity = dto.toSaveEntity();
            productRepository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //상품검색
//    public List<ProductResponseDto> productSearch(ProductSearchDto dto) {
//        String productCategory = dto.getProductCategory();
//        String keyWord = dto.getKeyWord();
//
//
//
//
//
//        }
//    }



}
