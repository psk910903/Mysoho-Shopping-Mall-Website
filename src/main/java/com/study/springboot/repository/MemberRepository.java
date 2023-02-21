package com.study.springboot.repository;

import com.study.springboot.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    @Query(value = "select * from member m where m.user_id = :user_id_param and m.user_pw = :user_pw_param", nativeQuery = true)
    List<MemberEntity> findByUserIdAndUserPw(@Param("user_id_param") String user_id,
                                             @Param("user_pw_param") String user_pw);


    @Query(value = "SELECT * FROM `member` WHERE member_no LIKE CONCAT('%',:keyword,'%') OR member_id LIKE CONCAT('%',:keyword,'%') or member_name LIKE CONCAT('%',:keyword,'%') or member_email LIKE CONCAT('%',:keyword,'%') or member_phone LIKE CONCAT('%',:keyword,'%') order BY `member_join_datetime`", nativeQuery = true)
    Page<MemberEntity> findByKeyword(@Param(value="keyword")String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM `member` WHERE member_rate = :findByType1 AND member_name LIKE CONCAT('%',:keyword,'%') order BY member_join_datetime desc", nativeQuery = true)
    Page<MemberEntity> findByType1(@Param(value="findByType1")String findByType1, @Param(value="keyword")String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM `member` WHERE member_id LIKE CONCAT('%',:keyword,'%') order BY member_join_datetime desc", nativeQuery = true)
    Page<MemberEntity> findByMemberId(@Param(value="keyword")String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM `member` WHERE member_name LIKE CONCAT('%',:keyword,'%') order BY member_join_datetime desc", nativeQuery = true)
    Page<MemberEntity> findByMemberName(@Param(value="keyword")String keyword, Pageable pageable);


    @Query(value = "SELECT * FROM `member` WHERE `member_rate` = :findByType1 AND member_name LIKE CONCAT('%',:keyword,'%') order BY member_join_datetime desc", nativeQuery = true)
    Page<MemberEntity> findByMemberName(@Param(value="findByType1")String findByType1, @Param(value="keyword")String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM `member` WHERE `member_rate` = :findByType1 AND member_id LIKE CONCAT('%',:keyword,'%') order BY member_join_datetime desc", nativeQuery = true)
    Page<MemberEntity> findByMemberId(@Param(value="findByType1")String findByType1, @Param(value="keyword")String keyword, Pageable pageable);


}
