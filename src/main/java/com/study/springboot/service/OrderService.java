package com.study.springboot.service;

import com.study.springboot.dto.order.OrderInfoSaveRequestDto;
import com.study.springboot.dto.order.OrderListResponseDto;
import com.study.springboot.entity.*;
import com.study.springboot.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    final OrderRepository orderRepository;

    //리스트 페이징
    @Transactional(readOnly = true)
    public Page<OrderEntity> getList(int page) {
//        List<Sort.Order> sorts = new ArrayList<>();
//        sorts.add(Sort.Order.desc("item_update_datetime")); //최신글을 먼저 보여준다.
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts)); //5개씩

        Pageable pageable = PageRequest.of(page, 5); //5개씩
        return orderRepository.findAll(pageable);
    }
    //페이징 5개로 고정
    @Transactional(readOnly = true)
    public List<Integer> getPageList(int page, Page<OrderEntity> paging) {
        int totalPage = paging.getTotalPages();
        List<Integer> pageList = new ArrayList<>();
        if (totalPage <= 5){
            for (Integer i=0; i<=totalPage-1; i++){
                pageList.add(i);
            }
            return pageList;
        }else if(page >= 0 && page <= 2){
            for (Integer i=0; i<=4; i++){
                pageList.add(i);
            }
            return pageList;
        }
        else if (page >= totalPage-3 && page <= totalPage-1){
            for (Integer i=5; i>=1; i--){
                pageList.add(totalPage - i);
            }
            return pageList;
        }else{
            for (Integer i=-2; i<=2; i++){
                pageList.add(page + i);
            }
            return pageList;
        }
    }
    //페이징에서 dto 리스트 꺼내기
    @Transactional(readOnly = true)
    public List<OrderListResponseDto> getDtoList(Page<OrderEntity> paging){
        List<OrderListResponseDto> list = new ArrayList<>();
        for (OrderEntity entity : paging) {
            list.add(new OrderListResponseDto(entity));
        }
        return list;
    }


    @Transactional(readOnly = true)
    public OrderListResponseDto findById(Long id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
        return new OrderListResponseDto(entity);
    }

    @Transactional
    public boolean updateOrderInfo(OrderInfoSaveRequestDto dto) {
        try {
            OrderEntity entity = orderRepository.findById(dto.getOrder_no()).get();
            OrderListResponseDto responseDto = new OrderListResponseDto(entity);
            dto.setOrder_datetime(responseDto.getOrder_datetime());
            orderRepository.save(dto.toEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Transactional
    public boolean stateUpdate(Long id, String orderState) {

        try {
            OrderListResponseDto dto = findById(id);
            dto.setOrder_state(orderState);
            orderRepository.save(dto.toEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
