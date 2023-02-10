package com.study.springboot.repository;

import com.study.springboot.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    //카테고리로 검색
    @Query(value = "select * from item where item_category like :category", nativeQuery = true)
    List<ProductEntity> findByProductCategory(String category);

}
