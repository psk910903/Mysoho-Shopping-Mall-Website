package com.study.springboot.service;

import com.study.springboot.dto.ReplyResponseDto;
import com.study.springboot.dto.ReplySaveRequestDto;
import com.study.springboot.entity.Reply;
import com.study.springboot.entity.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyService {
    final private ReplyRepository replyRepository;

    @Transactional(readOnly = true)
    public List<ReplyResponseDto> findAll(){
        Sort sort = Sort.by(Sort.Direction.DESC, "replyIdx", "replyDate");
        List<Reply> list = replyRepository.findAll(sort);
        //Entity 리스트 => DTO 리스트
        return list.stream().map(ReplyResponseDto::new).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<ReplyResponseDto> findAllByReplyBoardIdx(Long board_idx){

        List<Reply> list = replyRepository.findAllByReplyBoardIdx(board_idx);
        //Entity 리스트 => DTO 리스트
        return list.stream().map(ReplyResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public Long save(final ReplySaveRequestDto dto) {
        Reply entity = replyRepository.save( dto.toEntity() );
        return entity.getReplyIdx();
    }

    @Transactional(readOnly = true)
    public boolean existsById( Long reply_idx ) {
        boolean isFound = replyRepository.existsById(reply_idx);
        return isFound;
    }

    @Transactional
    public void delete(Long reply_idx){
        Reply entity = replyRepository.findById(reply_idx)
                .orElseThrow( () -> new IllegalArgumentException("해당 사용자가 없습니다. reply_idx=" + reply_idx));
        replyRepository.delete( entity );
    }

}
