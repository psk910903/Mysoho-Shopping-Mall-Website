package com.study.springboot.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

//1.엔티티에는 Setter를 넣지 않는다. DB접근하기 때문에, 원치 않는 Update발생
//  할 여지가 있음.
//  @Builder를 이용하여 데이타를 Set한다.
//2.DTO/VO 클래스에는 자유롭게 Setter/Getter 사용가능.

// 엔티티에 @Setter를 넣지 않는다. JPA 함수 호출시 중복호출되는 경향이 있다.
@Getter
//생성자의 접근제어자를 protected로 선언하면 new Board()
//   작성이 불가하기 때문에 객체 자체의 일관성 유지력을 높일 수 있다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_idx", nullable = false)
    private Long boardIdx; //PK
    @Column(name = "board_title", nullable = false)
    private String boardTitle; //제목
    @Column(name = "board_content", nullable = false)
    private String boardContent; //내용
    @Column(name = "board_name", nullable = false)
    private String boardName; //작성자
    @Column(name = "board_hit", nullable = false)
    private Long boardHit; //조회수
    @Column(name = "board_date", nullable = false)
    private LocalDateTime boardDate = LocalDateTime.now(); // 생성일,수정일

    @Builder
    public Board(String board_title, String board_content,
                 String board_name, Long board_hit) {
        this.boardTitle = board_title;
        this.boardContent = board_content;
        this.boardName = board_name;
        this.boardHit = board_hit;
    }
    public void update(String board_title, String board_content,
                        String board_name, Long board_hit) {
        this.boardTitle = board_title;
        this.boardContent = board_content;
        this.boardName = board_name;
        this.boardHit = board_hit;
        this.boardDate = LocalDateTime.now();
    }
}
