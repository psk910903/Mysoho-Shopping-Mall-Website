package com.study.springboot.dto.qna;

import com.study.springboot.entity.qna.AdminReplyEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminReplySaveDto {

    private Long id;
    private String adminreply_name;
    private String adminreply_content;
    private LocalDateTime adminreply_date;


    public AdminReplyEntity toSaveAdminReplyEntity(){
        return AdminReplyEntity.builder()
                .adminreply_name(adminreply_name)
                .adminreply_content(adminreply_content)
                .adminreply_date(adminreply_date)
                .build();
    }

}
