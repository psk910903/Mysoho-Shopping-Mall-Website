package com.study.springboot.repository;

import com.study.springboot.entity.OrderEntity;
import com.study.springboot.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query(value = "SELECT * FROM `item` WHERE item_no LIKE CONCAT('%',:keyword,'%') or item_name LIKE CONCAT('%',:keyword,'%') or item_option_color LIKE CONCAT('%',:keyword,'%') or item_option_size LIKE CONCAT('%',:keyword,'%') or item_price LIKE CONCAT('%',:keyword,'%') order BY `item_update_datetime` desc", nativeQuery = true)
    Page<ProductEntity> findByKeyword(@Param(value="keyword")String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM `item` WHERE item_category = :findByType1 AND item_name LIKE CONCAT('%',:keyword,'%') order BY item_update_datetime desc", nativeQuery = true)
    Page<ProductEntity> findByType1(@Param(value="findByType1")String findByType1, @Param(value="keyword")String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM `item` WHERE `item_name` LIKE CONCAT('%',:keyword,'%') order BY item_update_datetime desc", nativeQuery = true)
    Page<ProductEntity> findByItemName(@Param(value="keyword")String keyword, Pageable sort);

    @Query(value = "SELECT * FROM `item` WHERE `item_no` LIKE CONCAT('%',:keyword,'%') order BY item_update_datetime desc", nativeQuery = true)
    Page<ProductEntity> findByItemNo(@Param(value="keyword")String keyword, Pageable sort);

    @Query(value = "SELECT * FROM `item` WHERE `item_price` LIKE CONCAT('%',:keyword,'%') order BY item_update_datetime desc", nativeQuery = true)
    Page<ProductEntity> findByItemPrice(@Param(value="keyword")String keyword, Pageable sort);

    @Query(value = "SELECT * FROM `item` WHERE `item_category` = :findByType1 AND item_name LIKE CONCAT('%',:keyword,'%') order BY item_update_datetime desc", nativeQuery = true)
    Page<ProductEntity> findByItemName(@Param(value="findByType1")String findByType1, @Param(value="keyword")String keyword, Pageable sort);

    @Query(value = "SELECT * FROM `item` WHERE `item_category` = :findByType1 AND item_no LIKE CONCAT('%',:keyword,'%') order BY item_update_datetime desc", nativeQuery = true)
    Page<ProductEntity> findByItemNo(@Param(value="findByType1")String findByType1, @Param(value="keyword")String keyword, Pageable sort);

    @Query(value = "SELECT * FROM `item` WHERE `item_category` = :findByType1 AND item_price LIKE CONCAT('%',:keyword,'%') order BY item_update_datetime desc", nativeQuery = true)
    Page<ProductEntity> findByItemPrice(@Param(value="findByType1")String findByType1, @Param(value="keyword")String keyword, Pageable sort);



}
