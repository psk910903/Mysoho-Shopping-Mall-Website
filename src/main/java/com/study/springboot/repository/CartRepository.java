package com.study.springboot.repository;

import com.study.springboot.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    //네이티브 쿼리
    @Query(value = "SELECT * FROM cart WHERE cart_code = :cart_code", nativeQuery = true)
    CartEntity findByCartCodeNativeQuery(@Param("cart_code") String cart_code);
}
