package com.study.springboot.entity.repository;

import com.study.springboot.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    //네이티브 쿼리
    @Query(value = "SELECT * FROM cart WHERE cart_code = :cart_code", nativeQuery = true)
    CartEntity findByCart(@Param(value="cart_code")String cart_code);

    @Query(value = "SELECT * FROM cart WHERE cart_code = :cart_code and member_id is NULL", nativeQuery = true)
    CartEntity findByCartNonMember(@Param(value="cart_code")String cart_code);

    @Query(value = "SELECT * FROM cart WHERE cart_code = :cart_code and NOT member_id is NULL", nativeQuery = true)
    CartEntity findByCartMember(@Param(value="cart_code")String cart_code);

    @Query(value = "SELECT * FROM cart WHERE member_id = :id order BY cart_no desc" , nativeQuery = true)
    List<CartEntity> findByCartMemberId(@Param(value="id")String id);

    @Query(value = "SELECT COUNT(*) FROM cart WHERE item_code = :item_code", nativeQuery = true)
    int findByItemSortSale(@Param(value="item_code")Long item_code);

    @Query(value = "SELECT item_code FROM cart WHERE item_code GROUP BY item_code ORDER BY COUNT(item_code ) DESC LIMIT 6", nativeQuery = true)
    List<String> bestItemFindLimit6();
}
