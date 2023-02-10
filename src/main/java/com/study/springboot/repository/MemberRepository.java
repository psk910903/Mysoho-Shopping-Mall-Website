package com.study.springboot.repository;

import com.study.springboot.entity.Member.MemberEntity;
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


}
