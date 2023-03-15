package com.study.springboot.entity.repository;

import com.study.springboot.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    @Query(value = "select * from member m where m.member_name = :member_name_param and m.member_phone = :member_phone_param", nativeQuery = true)
    Optional<MemberEntity> findByMemberNameAndMemberPhone(@Param("member_name_param") String member_name,
                                                      @Param("member_phone_param") String member_phone);

    @Query(value = "select * from member m where m.member_email = :member_email_param", nativeQuery = true)
    Optional<MemberEntity> findByMemberEmail(@Param("member_email_param") String member_email);


    @Query(value = "SELECT * FROM `member` WHERE member_no LIKE CONCAT('%',:keyword,'%') OR member_id LIKE CONCAT('%',:keyword,'%') or member_name LIKE CONCAT('%',:keyword,'%') or member_email LIKE CONCAT('%',:keyword,'%') or member_phone LIKE CONCAT('%',:keyword,'%') order BY `member_join_datetime`", nativeQuery = true)
    Page<MemberEntity> findByKeyword(@Param(value="keyword")String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM `member` WHERE member_rate = :findByType1 AND member_id LIKE CONCAT('%',:keyword,'%') OR member_rate = :findByType1 AND member_name LIKE CONCAT('%',:keyword,'%') order BY member_join_datetime DESC", nativeQuery = true)
    Page<MemberEntity> findByType1(@Param(value="findByType1")String findByType1, @Param(value="keyword")String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM `member` WHERE member_id LIKE CONCAT('%',:keyword,'%') order BY member_join_datetime desc", nativeQuery = true)
    Page<MemberEntity> findByMemberId(@Param(value="keyword")String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM `member` WHERE member_name LIKE CONCAT('%',:keyword,'%') order BY member_join_datetime desc", nativeQuery = true)
    Page<MemberEntity> findByMemberName(@Param(value="keyword")String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM `member` WHERE `member_rate` = :findByType1 AND member_name LIKE CONCAT('%',:keyword,'%') order BY member_join_datetime desc", nativeQuery = true)
    Page<MemberEntity> findByMemberName(@Param(value="findByType1")String findByType1, @Param(value="keyword")String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM `member` WHERE `member_rate` = :findByType1 AND member_id LIKE CONCAT('%',:keyword,'%') order BY member_join_datetime desc", nativeQuery = true)
    Page<MemberEntity> findByMemberId(@Param(value="findByType1")String findByType1, @Param(value="keyword")String keyword, Pageable pageable);

    //회원가입용
    @Query(value = "select * from member m where m.member_id = :username_param", nativeQuery = true)
    Optional<MemberEntity> findByUserId(@Param("username_param") String username);

    // 희진 02 24 수정 ----------------------------------------------------------------

    @Query(value = "SELECT member_name FROM `member` WHERE `member_id` = :memberId", nativeQuery = true)
    String findMemberNameByMemberId(@Param(value="memberId")String memberId);

    @Query(value = "SELECT * FROM `member` WHERE `member_id` = :memberId", nativeQuery = true)
    Optional<MemberEntity> findByMemberId(@Param(value="memberId")String memberId);
}
