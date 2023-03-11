package com.study.springboot.service;

import com.study.springboot.comparator.LowPriceComparator;
import com.study.springboot.comparator.HighReviewComparator;
import com.study.springboot.comparator.ItemGradeComparator;
import com.study.springboot.comparator.SellCountComparator;
import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.inquiry.InquiryResponseDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.order.OrderSearchDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.qna.QnaResponseDto;
import com.study.springboot.dto.review.ReviewResponseDto;
import com.study.springboot.entity.CartEntity;
import com.study.springboot.entity.OrderEntity;
import com.study.springboot.entity.ProductEntity;
import com.study.springboot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Service1 {
    final ProductRepository productRepository;
    final OrderRepository orderRepository;
    final CartRepository cartRepository;
    final CartService cartService;
    final ReviewRepository reviewRepository;
    final Service5 service5;
    final Service4 service4;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByItem(int num) {
        List<ProductEntity> entityList;
        if (num == 6) { //BEST 6
            entityList = productRepository.findLimit6();
        } else { // 9
            entityList = productRepository.findLimit9();
        }

        List<ProductResponseDto> list = entityList.stream().map(ProductResponseDto::new).collect(Collectors.toList());
        return setItemDiscountPrice(list);
    }

    List<ProductResponseDto> setItemDiscountPrice(List<ProductResponseDto> list) {
        for (ProductResponseDto dto : list) {
            Long itemPrice = dto.getItemPrice();
            Long itemDiscountRate = dto.getItemDiscountRate();
            long price = (long) (Math.floor((itemPrice * ((100 - itemDiscountRate) * 0.01)) / 100)) * 100;
            dto.setItemDiscountPrice(price);
        }
        return list;
    }

    //정렬
    @Transactional(readOnly = true)
    public List<ProductResponseDto> SortItem(List<ProductResponseDto> dtoList, String type) {
        List<ProductResponseDto> list = new ArrayList<>();
        for (ProductResponseDto dto : dtoList) {
            list.add(dto.clone());
        }

        if (type.equals("판매량")) {
            for (ProductResponseDto dto : list) {
                dto.setSalesCount(cartRepository.findByItemSortSale(dto.getItemNo()));
            }
            list.sort(new SellCountComparator());
        } else if (type.equals("낮은가격")) {
            list.sort(new LowPriceComparator());
        } else if (type.equals("리뷰")) {
            for (ProductResponseDto dto : list) {
                dto.setReviewCount(reviewRepository.findByItemReview(dto.getItemNo()));
            }
            list.sort(new HighReviewComparator());
        } else if (type.equals("평점")) {
            for (ProductResponseDto dto : list) {
                Integer reviewStarAVG = reviewRepository.findByItemReviewStarAVG(dto.getItemNo());
                if (reviewStarAVG == null) { dto.setReviewStar(0);} else { dto.setReviewStar(reviewStarAVG); }
            }
            list.sort(new ItemGradeComparator());
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByKeyword(String keyword) {
        List<ProductEntity> list = productRepository.findByItemNameContaining(keyword);
        return list.stream().map(ProductResponseDto::new).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<ProductResponseDto> findByCategory(String keyword) {
        List<ProductEntity> entityList;
        if (keyword.equals("SALE")) {
            entityList = productRepository.findByCategorySale(keyword);
        } else {
            entityList = productRepository.findByCategory(keyword);
        }
        List<ProductResponseDto> list = entityList.stream().map(ProductResponseDto::new).collect(Collectors.toList());
        return setItemDiscountPrice(list);
    }


    @Transactional(readOnly = true)
    public List<OrderResponseDto> findByOrderNonMember(OrderSearchDto dto) {
        String sender = dto.getSender();
        String phone = dto.getPhone1() + dto.getPhone2();

        List<OrderEntity> orderEntity = orderRepository.findByOrderNonMember(sender, phone);

        return orderEntity.stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CartResponseDto> getCartList(OrderResponseDto dto) {
        List<CartResponseDto> cartList = new ArrayList<>();
        if (dto.getCartCode1() != null) {
            //주문정보의 장바구니 코드로 장바구니 정보 dto 생성
            CartResponseDto responseDto = cartService.findByCart(dto.getCartCode1());
            //장바구니 리스트에 셋팅된 dto 담기
            cartList.add(responseDto);
        }
        if (dto.getCartCode2() != null) {
            //반복
            cartList.add(cartService.findByCart(dto.getCartCode2()));
        }
        if (dto.getCartCode3() != null) {
            cartList.add(cartService.findByCart(dto.getCartCode3()));
        }
        if (dto.getCartCode4() != null) {
            cartList.add(cartService.findByCart(dto.getCartCode4()));
        }
        if (dto.getCartCode5() != null) {
            cartList.add(cartService.findByCart(dto.getCartCode5()));
        }
        return cartList;
    }


    @Transactional(readOnly = true)
    public List<CartResponseDto> getCartListNonMember(OrderResponseDto dto) {
        List<CartResponseDto> cartList = new ArrayList<>();

        if (dto.getCartCode1() != null) {
            //주문정보의 장바구니 코드로 장바구니 정보 dto 생성
            CartResponseDto cartDto = cartService.findByCartNonMember(dto.getCartCode1());
            //장바구니에 상품 이미지 url 셋팅
            cartDto.setItemImageUrl(productRepository.findByUrl(cartDto.getItemCode()));
            //장바구니 리스트에 셋팅된 dto 담기
            cartList.add(cartDto);
        }
        if (dto.getCartCode2() != null) {
            //2~5 카트 반복
            CartResponseDto cartDto = cartService.findByCartNonMember(dto.getCartCode2());
            cartDto.setItemImageUrl(productRepository.findByUrl(cartDto.getItemCode()));
            cartList.add(cartDto);
        }
        if (dto.getCartCode3() != null) {
            CartResponseDto cartDto = cartService.findByCartNonMember(dto.getCartCode3());
            cartDto.setItemImageUrl(productRepository.findByUrl(cartDto.getItemCode()));
            cartList.add(cartDto);
        }if (dto.getCartCode4() != null) {
            CartResponseDto cartDto = cartService.findByCartNonMember(dto.getCartCode4());
            cartDto.setItemImageUrl(productRepository.findByUrl(cartDto.getItemCode()));
            cartList.add(cartDto);
        }if (dto.getCartCode5() != null) {
            CartResponseDto cartDto = cartService.findByCartNonMember(dto.getCartCode5());
            cartDto.setItemImageUrl(productRepository.findByUrl(cartDto.getItemCode()));
            cartList.add(cartDto);
        }

        return cartList;
    }



    @Transactional(readOnly = true)
    public List<CartResponseDto> getCartListMember(OrderResponseDto dto) {
        List<CartResponseDto> cartList = new ArrayList<>();
        if (dto.getCartCode1() != null) {
            //주문정보의 장바구니 코드로 장바구니 정보 dto 생성
            CartResponseDto cartDto = cartService.findByCartMember(dto.getCartCode1());
            //장바구니에 상품 이미지 url 셋팅
            String byUrl = productRepository.findByUrl(cartDto.getItemCode());
            cartDto.setItemImageUrl(byUrl);
            //장바구니 리스트에 셋팅된 dto 담기
            cartList.add(cartDto);
        }
        if (dto.getCartCode2() != null) {
            //반복
            CartResponseDto cartDto = cartService.findByCartMember(dto.getCartCode2());

            String byUrl = productRepository.findByUrl(cartDto.getItemCode());
            cartDto.setItemImageUrl(byUrl);
            cartList.add(cartDto);
        }
        if (dto.getCartCode3() != null) {
            CartResponseDto cartDto = cartService.findByCartMember(dto.getCartCode3());

            String byUrl = productRepository.findByUrl(cartDto.getItemCode());
            cartDto.setItemImageUrl(byUrl);
            cartList.add(cartDto);
        }
        if (dto.getCartCode4() != null) {
            CartResponseDto cartDto = cartService.findByCartMember(dto.getCartCode4());

            String byUrl = productRepository.findByUrl(cartDto.getItemCode());
            cartDto.setItemImageUrl(byUrl);
            cartList.add(cartDto);
        }
        if (dto.getCartCode5() != null) {
            CartResponseDto cartDto = cartService.findByCartMember(dto.getCartCode5());

            String byUrl = productRepository.findByUrl(cartDto.getItemCode());
            cartDto.setItemImageUrl(byUrl);
            cartList.add(cartDto);
        }

        return cartList;
    }

    @Transactional(readOnly = true)
    public List<CartEntity> getCartEntityListMember(OrderResponseDto dto) {
        List<CartEntity> cartList = new ArrayList<>();
        if (dto.getCartCode1() != null) {
            CartEntity entity = cartService.findByCartMemberEntity(dto.getCartCode1());
            cartList.add(entity);
        }
        if (dto.getCartCode2() != null) {
            CartEntity entity = cartService.findByCartMemberEntity(dto.getCartCode2());
            cartList.add(entity);
        }
        if (dto.getCartCode3() != null) {
            CartEntity entity = cartService.findByCartMemberEntity(dto.getCartCode3());
            cartList.add(entity);
        }
        if (dto.getCartCode4() != null) {
            CartEntity entity = cartService.findByCartMemberEntity(dto.getCartCode4());
            cartList.add(entity);
        }
        if (dto.getCartCode5() != null) {
            CartEntity entity = cartService.findByCartMemberEntity(dto.getCartCode5());
            cartList.add(entity);
        }

        return cartList;
    }

    //리뷰 아이디 마스킹
    public List<String> maskingId(List<ReviewResponseDto> list) {
        List<String> photoReviewIdList = new ArrayList<>();
        for(int i=0 ; i < list.size();i++){

            String reviewId = list.get(i).getMemberId();

            String reviewHiddenId;
            if (reviewId.length() == 2){
                reviewHiddenId = reviewId.replace(reviewId.charAt(1), '*');
            }else if(reviewId.length() == 1){
                reviewHiddenId = reviewId;
            }
            else{
                reviewHiddenId = reviewId.substring(0,2);
                //
                for (int j=0; j<reviewId.length()-2; j++){
                    reviewHiddenId += "*";
                }
            }
            photoReviewIdList.add(reviewHiddenId);
        }
        return photoReviewIdList;
    }

    public List<String> qnaMaskingId(List<QnaResponseDto> list) {
        List<String> nameList = new ArrayList<>();
        for(int i=0 ; i < list.size();i++){

            String qnaName = list.get(i).getMemberId();

            if(qnaName == null){
                qnaName = list.get(i).getQnaName();

            }
            String qnaHiddenName;
            if (qnaName.length() == 2){
                qnaHiddenName = qnaName.replace(qnaName.charAt(1), '*');
            }else if(qnaName.length() == 1){
                qnaHiddenName = qnaName;
            }
            else{
                qnaHiddenName = qnaName.substring(0,2);
                //
                for (int j=0; j<qnaName.length()-2; j++){
                    qnaHiddenName += "*";
                }
            }
            nameList.add(qnaHiddenName);
        }
        return nameList;
    }

    //상품문의 아이디 마스킹
    public List<String> inquiryMaskingId(List<InquiryResponseDto> list) {
        List<String> nameList = new ArrayList<>();
        for(int i=0 ; i < list.size();i++){

            String qnaName = list.get(i).getMemberId();
            if(qnaName == null){
                qnaName = list.get(i).getInquiryNickname();
            }
            String qnaHiddenName;
            if (qnaName.length() == 2){
                qnaHiddenName = qnaName.replace(qnaName.charAt(1), '*');
            }else if(qnaName.length() == 1){
                qnaHiddenName = qnaName;
            }
            else{
                qnaHiddenName = qnaName.substring(0,2);;
                //
                for (int j=0; j<qnaName.length()-2; j++){
                    qnaHiddenName += "*";
                }
            }
            nameList.add(qnaHiddenName);
        }
        return nameList;
    }

    public List<Long> inReplyCount(List<InquiryResponseDto> inquiry){
        List<Long> inReplyCount = new ArrayList<>();

        for(int i =0; i< inquiry.size(); i++){
            Long CommentCount = service5.countByInquiryNo(inquiry.get(i).getInquiryNo());
            inReplyCount.add(CommentCount);
        }
        return inReplyCount;
    }

    public List<Long> qnaCommentCount(List<QnaResponseDto> list){
        List<Long> qnaCommentCount = new ArrayList<>();
        for(int i =0; i< list.size(); i++){
            Long CommentCount = service4.countByQnaId(list.get(i).getQnaId());
            qnaCommentCount.add(CommentCount);
        }
        return qnaCommentCount;
    }

    //평점
    public Double avgStar(List<ReviewResponseDto> reviewList) {
        int size = reviewList.size();  // 3. 상품 리뷰 갯수

        byte sum = 0;       // 4. 상품별점 평균
        for(int i=0; i<size; i++){
            byte reviewStar = reviewList.get(i).getReviewStar();
            sum += reviewStar;
        }
        double avg1 = sum / Double.valueOf(size);
        double avg2 = Math.round(avg1*10);
        double avgStar = avg2 / 10;
        return avgStar;
    }


}
