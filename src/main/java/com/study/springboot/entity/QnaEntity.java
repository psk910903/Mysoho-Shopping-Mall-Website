package com.study.springboot.entity.qna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name="Qna")
public class QnaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 번호
    @Column
    private String qna_title; // 제목
    @Column
    private String qna_content; // 내용
    @Column
    private String qna_password; // 비밀번호
    @Column
    private String qna_name; // 작성자
    @Column
    private Long qna_hit =0L; // 조회 수
    @Column
    private LocalDateTime qna_date = LocalDateTime.now(); // 생성일,수정일
//    private Long qna_itemcode; //상품코드


    @Builder
    public QnaEntity(Long id, String qna_title, String qna_content, String qna_password, String qna_name,
                     Long qna_hit, LocalDateTime qna_date ) { //Long qna_itemcode

        this.id = id;
        this.qna_title = qna_title;
        this.qna_content = qna_content;
        this.qna_password = qna_password;
        this.qna_name = qna_name;
//        this.qna_itemcode= qna_itemcode;
    }


}
