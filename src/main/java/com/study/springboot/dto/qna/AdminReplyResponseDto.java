package com.study.springboot.dto.qna;

import com.study.springboot.entity.qna.AdminReplyEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminReplyResponseDto {
    private Long adminreply_id;
    private String adminreply_name;
    private String adminreply_content;
    private LocalDateTime adminreply_date;

    public AdminReplyResponseDto(AdminReplyEntity adminreply) {
        this.adminreply_id = adminreply.getId();
        this.adminreply_name =adminreply.getAdminreply_name();
        this.adminreply_content = adminreply.getAdminreply_content();
        this.adminreply_date = adminreply.getAdminreply_date();
    }

}
