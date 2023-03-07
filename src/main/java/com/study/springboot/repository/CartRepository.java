package com.study.springboot.repository;

import com.study.springboot.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    //네이티브 쿼리
    @Query(value = "SELECT * FROM cart WHERE cart_code = :cart_code", nativeQuery = true)
    CartEntity findByCart(String cart_code);


    @Query(value = "SELECT * FROM cart WHERE cart_code = :cart_code and member_id is NULL", nativeQuery = true)
    CartEntity findByCartNonMember(String cart_code);

    @Query(value = "SELECT * FROM cart WHERE cart_code = :cart_code and NOT member_id is NULL", nativeQuery = true)
    CartEntity findByCartMember(String cart_code);

    @Query(value = "SELECT * FROM cart WHERE member_id = :id", nativeQuery = true)
    List<CartEntity> findByCartMemberId(String id);

    @Query(value = "SELECT COUNT(*) FROM cart WHERE item_code = :item_code", nativeQuery = true)
    int findByItemSortSale(Long item_code);

    @Query(value = "SELECT cart_no FROM cart WHERE order_code = :order_code", nativeQuery = true)
    List<Long> findByOrderCode ( Long order_code );



}
