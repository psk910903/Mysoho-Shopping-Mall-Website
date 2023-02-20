package com.study.springboot.repository;

import com.study.springboot.entity.OrderEntity;
import com.study.springboot.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query(value = "SELECT * FROM `order` WHERE `order_no` LIKE CONCAT('%',:keyword,'%') or `cart_code_1` LIKE CONCAT('%',:keyword,'%') or `order_total_price` LIKE CONCAT('%',:keyword,'%') or `order_total_count` LIKE CONCAT('%',:keyword,'%') or `order_name` LIKE CONCAT('%',:keyword,'%') or `order_phone` LIKE CONCAT('%',:keyword,'%') or `order_recipient_name` LIKE CONCAT('%',:keyword,'%') or `order_recipient_phone` LIKE CONCAT('%',:keyword,'%') or `order_recipient_addr_number` LIKE CONCAT('%',:keyword,'%') or `order_recipient_addr_1` LIKE CONCAT('%',:keyword,'%') or `order_recipient_addr_2` LIKE CONCAT('%',:keyword,'%') or `order_pay_type` LIKE CONCAT('%',:keyword,'%') order BY `order_datetime`", nativeQuery = true)
    Page<OrderEntity> findByAllContaining(@Param(value="keyword")String keyword, Pageable sort);

    Page<OrderEntity> findByOrderNameContaining(String keyword, Pageable sort);

    Page<OrderEntity> findByOrderPhoneContaining(String keyword, Pageable sort);

    //주문번호 검색
    @Query(value = "SELECT * FROM `order` WHERE order_no LIKE CONCAT('%',:keyword,'%') order BY order_datetime desc", nativeQuery = true)
    Page<OrderEntity> findByOrderNoContaining(@Param(value="keyword")String keyword, Pageable sort);

    Page<OrderEntity> findByOrderState(String keyword, Pageable sort);

    //날짜로 검색
    @Query(value = "SELECT * FROM `order` WHERE order_datetime BETWEEN :start AND :end order BY order_datetime desc", nativeQuery = true)
    Page<OrderEntity> findByOrderNoContaining(@Param(value="start")String start, @Param(value="end")String end, Pageable sort);
}
