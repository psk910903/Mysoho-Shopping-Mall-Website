package com.study.springboot.entity.qna;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "AdminReply")
public class AdminReplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String adminreply_name;
    @Column
    private String adminreply_content;
    @Column
    private LocalDateTime adminreply_date = LocalDateTime.now();

    @Builder
    public AdminReplyEntity(Long id, String adminreply_name, String adminreply_content,
                            LocalDateTime adminreply_date) {
        this.id = id;
        this.adminreply_name = adminreply_name;
        this.adminreply_content = adminreply_content;
        this.adminreply_date = adminreply_date;
    }
}
