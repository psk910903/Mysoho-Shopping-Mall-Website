package com.study.springboot.repository;

import com.study.springboot.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    //네이티브 쿼리
    @Query(value = "SELECT * FROM cart WHERE cart_code = :cart_code", nativeQuery = true)
    CartEntity findByCart(String cart_code);

    // 02-26 희진 수정 -------------------------------------------------------------
    List<CartEntity> findByMemberId(String memberId);

    List<CartEntity> findBySessionId(String sessionId);


}
