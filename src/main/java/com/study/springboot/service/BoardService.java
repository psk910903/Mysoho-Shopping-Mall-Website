package com.study.springboot.service;

import com.study.springboot.dto.BoardResponseDto;
import com.study.springboot.dto.BoardSaveRequestDto;
import com.study.springboot.entity.Board;
import com.study.springboot.entity.BoardRepository;
import com.study.springboot.entity.Reply;
import com.study.springboot.entity.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//  HTTP요청--> @Controller -> @Service -> DTO/Entity -> @Repository(DAO) -> DBMS
//  HTTP응답(html,json) <- @Controller <- @Service <- DTO/Entity <- @Repository(DAO) <- DBMS

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    //게시글 목록 조회 : 페이징
    @Transactional(readOnly = true)
    public Page<Board> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("boardDate")); //최신글을 먼저 보여준다.

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); //10개씩
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    //JPA를 사용한다면, 서비스(Service) 클래스에서 필수적으로 사용되어야 하는 어노테이션입니다.
    //일반적으로 메서드 레벨에 선언하게 되며, 메서드의 실행, 종료, 예외를 기준으로
    //각각 실행(begin), 종료(commit), 예외(rollback)를 자동으로 처리해 줍니다.
    // Transaction : 일련의 작업의 단위(단일작업의 모음-Select, Update, Insert, Delete, Drop, Alter)
    // 트랜잭션 시작 : 일련의 작업의 시작
    // 트랜잭션 종료 : 일련의 작업의 종료(커밋 발생 - 물리DB에 적용)
    // 트랜잭션 예외 : 일련의 작업중 예외발생(로백 발생 - 처음 작업시작전 상태로 되돌림)
    // 예) 은행 송금 처리(하나의 트랜잭션)
    //    1. A계좌 : 1000원 감소  -> 성공
    //    2. B계좌 : 1000원 증가  -> 통신오류!
    //    3. 송금 내역 저장
    public List<BoardResponseDto> findAll() {
        //정렬기능 추가
        Sort sort = Sort.by(Sort.Direction.DESC, "boardIdx", "boardDate");
        List<Board> list = boardRepository.findAll(sort);

        //List<Board>를 List<BoardResponseDto>로 변환
        return list.stream().map(BoardResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public Long save(final BoardSaveRequestDto dto){
        Board entity = boardRepository.save( dto.toEntity() );
        return entity.getBoardIdx();
    }

    @Transactional(readOnly = true)
    public boolean existsById( Long board_idx ) {
        boolean isFound = boardRepository.existsById( board_idx );
        return isFound;
    }
    @Transactional(readOnly = true)
    public BoardResponseDto findById(Long board_idx) {
        Board entity = boardRepository.findById( board_idx )
                        .orElseThrow( () -> new IllegalArgumentException("해당 사용자가 없습니다. board_idx="+board_idx) );

        return new BoardResponseDto(entity);
    }
    @Transactional
    public Board update(final Long board_idx, final BoardSaveRequestDto dto) {
        Board entity = boardRepository.findById(board_idx)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. board_idx="+board_idx) );

        entity.update(dto.getBoard_title(), dto.getBoard_content(),
                     dto.getBoard_name(), dto.getBoard_hit());

        Board new_entity = boardRepository.save( entity );

        return new_entity;
    }
    @Transactional
    public void delete(final Long board_idx) {
        Board entity = boardRepository.findById(board_idx)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. board_idx="+board_idx) );
        boardRepository.delete( entity );

        //글에 달린 댓글도 삭제
        List<Reply> reply_list = replyRepository.findAllByReplyBoardIdx(board_idx);

        for(Reply reply_entity : reply_list) {
            replyRepository.delete( reply_entity );
        }
    }
    
    
}
