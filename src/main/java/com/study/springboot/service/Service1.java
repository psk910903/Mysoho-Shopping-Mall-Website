package com.study.springboot.service;

import com.study.springboot.comparator.LowPriceComparator;
import com.study.springboot.comparator.HighReviewComparator;
import com.study.springboot.comparator.ItemGradeComparator;
import com.study.springboot.comparator.SellCountComparator;
import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.order.OrderSearchDto;
import com.study.springboot.dto.product.ProductResponseDto;
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
    final ProductService productService;
    final OrderRepository orderRepository;
    final CartRepository cartRepository;
    final CartService cartService;
    final ReviewRepository reviewRepository;

    final NoticeRepository noticeRepository;

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
    public Long getTotalPrice(List<CartResponseDto> cartList) {
        Long totalPrice = 0L;
        for (CartResponseDto dto : cartList) {
            totalPrice += dto.getCartItemPrice();
        }
        return totalPrice;
    }

    @Transactional(readOnly = true)
    public Long getTotalCount(List<CartResponseDto> cartList) {
        Long totalCount = 0L;
        for (CartResponseDto dto : cartList) {
            totalCount += dto.getCartItemAmount();
        }
        return totalCount;
    }

    @Transactional(readOnly = true)
    public String findLatestNotice(){
        String noticeTitle = noticeRepository.findLatestNotice();
        return noticeTitle;
    };

}
