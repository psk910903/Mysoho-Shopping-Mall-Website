package com.study.springboot.service;

import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.cart.CartSaveRequestDto;
import com.study.springboot.dto.order.OrderResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.entity.CartEntity;
import com.study.springboot.entity.OrderEntity;
import com.study.springboot.entity.repository.CartRepository;
import com.study.springboot.entity.repository.OrderRepository;
import com.study.springboot.entity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public CartResponseDto findByCart(String cartCode) {
        return new CartResponseDto(cartRepository.findByCart(cartCode));
    }
    @Transactional(readOnly = true)
    public CartResponseDto findByCartNonMember(String cartCode) {
        return new CartResponseDto(cartRepository.findByCartNonMember(cartCode));
    }

    @Transactional(readOnly = true)
    public CartResponseDto findByCartMember(String cartCode) {
        return new CartResponseDto(cartRepository.findByCartMember(cartCode));
    }

    @Transactional(readOnly = true)
    public CartEntity findByCartMemberEntity(String cartCode) {
        return cartRepository.findByCartMember(cartCode);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> findByOrderList(String id) {
        //회원 아이디로 카트테이블에서 정보 가져오기
        List<CartEntity> cartEntityList = cartRepository.findByCartMemberId(id);
        //entity -> dto로 변환
        List<CartResponseDto> cartDtoList = cartEntityList.stream().map(CartResponseDto::new).toList();
        //카트 테이블에서 주문번호만 추출해서 담을 orderCodeList 생성
        List<Long> orderCodeList = new ArrayList<>();
        //반복해서 주문번호만 추출 후 리스트에 담기
        for (CartResponseDto cartDto : cartDtoList) {
            orderCodeList.add(cartDto.getOrderCode());
        }
        //주문번호리스트에서 중복 값 제거
        List<Long> newList = orderCodeList.stream().distinct().toList();
        //주문번호들로 주문테이블에서 정보 가져와서 담을 리스트 생성
        List<OrderResponseDto> orderList = new ArrayList<>();
        //반복해서 주문테이블 객체 가져와서 dto로 변환 후 리스트에 담기
        for (Long orderCode : newList) {
            OrderEntity orderEntity = orderRepository.findByOrderCode(orderCode).get();
            OrderResponseDto dto = new OrderResponseDto(orderEntity);
            orderList.add(dto);
        }
        return orderList;


    }
    //entity로 반환하는 함수
    @Transactional(readOnly = true)
    public List<OrderEntity> findByOrderListEntity(String memberId) {
        //회원 아이디로 카트테이블에서 정보 가져오기
        List<CartEntity> cartEntityList = cartRepository.findByCartMemberId(memberId);
        //entity -> dto로 변환
        List<CartResponseDto> cartDtoList = cartEntityList.stream().map(CartResponseDto::new).toList();
        //카트 테이블에서 주문번호만 추출해서 담을 orderCodeList 생성
        List<Long> orderCodeList = new ArrayList<>();
        //반복해서 주문번호만 추출 후 리스트에 담기
        for (CartResponseDto cartDto : cartDtoList) {
            orderCodeList.add(cartDto.getOrderCode());
        }
        //주문번호리스트에서 중복 값 제거
        List<Long> newList = orderCodeList.stream().distinct().toList();
        //주문번호들로 주문테이블에서 정보 가져와서 담을 리스트 생성
        List<OrderEntity> orderList = new ArrayList<>();
        //반복해서 주문테이블 객체 가져와서 dto로 변환 후 리스트에 담기
        for (Long orderCode : newList) {
            OrderEntity orderEntity = orderRepository.findByOrderCode(orderCode).get();
            orderList.add(orderEntity);
        }
        return orderList;
    }

    @Transactional(readOnly = true)
    public List<CartResponseDto> getCartList(OrderResponseDto dto) {
        List<CartResponseDto> cartList = new ArrayList<>();
        if (dto.getCartCode1() != null) {
            //주문정보의 장바구니 코드로 장바구니 정보 dto 생성
            CartResponseDto responseDto = findByCart(dto.getCartCode1());
            //장바구니 리스트에 셋팅된 dto 담기
            cartList.add(responseDto);
        }
        if (dto.getCartCode2() != null) {
            //반복
            cartList.add(findByCart(dto.getCartCode2()));
        }
        if (dto.getCartCode3() != null) {
            cartList.add(findByCart(dto.getCartCode3()));
        }
        if (dto.getCartCode4() != null) {
            cartList.add(findByCart(dto.getCartCode4()));
        }
        if (dto.getCartCode5() != null) {
            cartList.add(findByCart(dto.getCartCode5()));
        }
        return cartList;
    }


    @Transactional(readOnly = true)
    public List<CartResponseDto> getCartList(OrderResponseDto dto, String type) {
        List<CartResponseDto> cartList = new ArrayList<>();

        if (dto.getCartCode1() != null) {
            //주문정보의 장바구니 코드로 장바구니 정보 dto 생성
            CartResponseDto cartDto;
            //회원일 때
            if (type.equals("member")) { cartDto = findByCartMember(dto.getCartCode1()); }
            //비회원일 때
            else { cartDto = findByCartNonMember(dto.getCartCode1());}
            //장바구니에 상품 이미지 url 셋팅
            cartDto.setItemImageUrl(productRepository.findByUrl(cartDto.getItemCode()));
            //장바구니 리스트에 셋팅된 dto 담기
            cartList.add(cartDto);
        }
        if (dto.getCartCode2() != null) {
            //2~5 카트 반복
            CartResponseDto cartDto;
            if (type.equals("member")) { cartDto = findByCartMember(dto.getCartCode2()); }
            else { cartDto = findByCartNonMember(dto.getCartCode2());}
            cartDto.setItemImageUrl(productRepository.findByUrl(cartDto.getItemCode()));
            cartList.add(cartDto);
        }
        if (dto.getCartCode3() != null) {
            CartResponseDto cartDto;
            if (type.equals("member")) { cartDto = findByCartMember(dto.getCartCode3()); }
            else { cartDto = findByCartNonMember(dto.getCartCode3());}
            cartDto.setItemImageUrl(productRepository.findByUrl(cartDto.getItemCode()));
            cartList.add(cartDto);
        }if (dto.getCartCode4() != null) {
            CartResponseDto cartDto;
            if (type.equals("member")) { cartDto = findByCartMember(dto.getCartCode4()); }
            else { cartDto = findByCartNonMember(dto.getCartCode4());}
            cartDto.setItemImageUrl(productRepository.findByUrl(cartDto.getItemCode()));
            cartList.add(cartDto);
        }if (dto.getCartCode5() != null) {
            CartResponseDto cartDto;
            if (type.equals("member")) { cartDto = findByCartMember(dto.getCartCode5()); }
            else { cartDto = findByCartNonMember(dto.getCartCode5());}
            cartDto.setItemImageUrl(productRepository.findByUrl(cartDto.getItemCode()));
            cartList.add(cartDto);
        }

        return cartList;
    }

    @Transactional(readOnly = true)
    public List<CartEntity> getCartEntityListMember(OrderResponseDto dto) {
        List<CartEntity> cartList = new ArrayList<>();
        if (dto.getCartCode1() != null) {
            CartEntity entity = findByCartMemberEntity(dto.getCartCode1());
            cartList.add(entity);
        }
        if (dto.getCartCode2() != null) {
            CartEntity entity = findByCartMemberEntity(dto.getCartCode2());
            cartList.add(entity);
        }
        if (dto.getCartCode3() != null) {
            CartEntity entity = findByCartMemberEntity(dto.getCartCode3());
            cartList.add(entity);
        }
        if (dto.getCartCode4() != null) {
            CartEntity entity = findByCartMemberEntity(dto.getCartCode4());
            cartList.add(entity);
        }
        if (dto.getCartCode5() != null) {
            CartEntity entity = findByCartMemberEntity(dto.getCartCode5());
            cartList.add(entity);
        }

        return cartList;
    }

    //장바구니 정보 db 저장
    @Transactional
    public Boolean saveCart(final CartSaveRequestDto dto) {

        try{
            CartEntity entity = dto.toEntity();
            cartRepository.save(entity);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Transactional
    public CartSaveRequestDto saveCartDto(String cartCode, Long orderCode, String itemCode,
                                           Long cartItemAmount, String itemOptionColor, String itemOptionSize, String memberId) {
            ProductResponseDto productResponseDto = productService.findById(Long.parseLong(itemCode));

            // cartDiscountPrice, cartItemPrice
            Long cartDiscountPrice = productResponseDto.getItemPrice() * productResponseDto.getItemDiscountRate() / 100;
            Long cartItemPrice = (productResponseDto.getItemPrice() - cartDiscountPrice) / 100 * 100;
            cartDiscountPrice = productResponseDto.getItemPrice() - cartItemPrice;

        CartSaveRequestDto cartSaveRequestDto = CartSaveRequestDto.builder()
                    .cartCode(cartCode)
                    .orderCode(orderCode)
                    .memberId(memberId)
                    .itemCode(itemCode)
                    .itemName(productResponseDto.getItemName())
                    .itemOptionColor(itemOptionColor)
                    .itemOptionSize(itemOptionSize)
                    .cartItemAmount(cartItemAmount)
                    .cartItemOriginalPrice(productResponseDto.getItemPrice())
                    .cartDiscountPrice(cartDiscountPrice)
                    .cartItemPrice(cartItemPrice)
                    .cartDate(LocalDateTime.now())
                    .build();
        return cartSaveRequestDto;
    }

    public long[] priceSetting(List<CartResponseDto> cartList, OrderResponseDto orderDto){
        long originalPrice = 0L;
        long discountPrice = 0L;
        long itemPrice = 0L;

        for (CartResponseDto cartDto : cartList) {

            if (Objects.equals(cartDto.getOrderCode(), orderDto.getOrderCode())) {
                originalPrice += cartDto.getCartItemOriginalPrice() * cartDto.getCartItemAmount();
                discountPrice += cartDto.getCartDiscountPrice() * cartDto.getCartItemAmount();
                itemPrice += cartDto.getCartItemPrice() * cartDto.getCartItemAmount();
            }
        }
        long[] priceSetting = {originalPrice, discountPrice, itemPrice};
        return priceSetting;
    };
}
