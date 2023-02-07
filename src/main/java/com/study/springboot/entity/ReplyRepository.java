package com.study.springboot.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    //외래키-글인덱스(board_idx)로 댓글찾기
    List<Reply> findAllByReplyBoardIdx(Long replyBoardIdx);
}
