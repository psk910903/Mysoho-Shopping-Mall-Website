package com.study.springboot.entity.repository;

import com.study.springboot.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    //----------------------------------------------------------------------------------------------------------------------

    @Query(value = "SELECT * FROM item where item_exposure='노출함' AND NOT item_sold_out='품절' AND item_no = :item_no", nativeQuery = true)
    Optional<ProductEntity> findBestItem(@Param(value="item_no")String item_no);

    @Query(value = "SELECT * FROM item where item_exposure='노출함' order BY item_update_datetime desc LIMIT 9;", nativeQuery = true)
    List<ProductEntity> findLimit9();

    @Query(value = "SELECT * FROM item where item_exposure='노출함' order BY item_update_datetime desc LIMIT 6;", nativeQuery = true)
    List<ProductEntity> findLimit6();

    @Query(value = "SELECT * FROM `item` WHERE item_name LIKE CONCAT('%',:keyword,'%') and item_exposure='노출함' order BY `item_name` desc", nativeQuery = true)
    List<ProductEntity> findByItemNameContaining(@Param(value="keyword")String keyword);

    @Query(value = "SELECT * FROM item where item_category=:keyword AND item_sold_out='판매중' AND item_exposure='노출함' order BY item_update_datetime DESC ;", nativeQuery = true)
    List<ProductEntity> findByCategory(@Param(value="keyword")String keyword);

    @Query(value = "SELECT * FROM item where item_discount_rate BETWEEN 1 AND 100 and item_exposure='노출함' order BY item_update_datetime DESC;", nativeQuery = true)
    List<ProductEntity> findByCategorySale(@Param(value="keyword")String keyword);

    //---
    @Query(value = "SELECT item_image_url FROM `item` WHERE item_no LIKE :keyword order BY `item_name` desc", nativeQuery = true)
    String findByUrl(@Param(value="keyword")String keyword);

    @Query(value = "SELECT item_image_url FRom `item` WHERE item_no = :item_no",nativeQuery = true)
    String findByItemNo(String item_no);



}
