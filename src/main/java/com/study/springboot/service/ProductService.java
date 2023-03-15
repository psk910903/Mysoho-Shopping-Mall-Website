package com.study.springboot.service;

import com.study.springboot.comparator.HighReviewComparator;
import com.study.springboot.comparator.ItemGradeComparator;
import com.study.springboot.comparator.LowPriceComparator;
import com.study.springboot.comparator.SellCountComparator;
import com.study.springboot.dto.cart.CartResponseDto;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.product.ProductSaveRequestDto;
import com.study.springboot.entity.ProductEntity;
import com.study.springboot.entity.repository.CartRepository;
import com.study.springboot.entity.repository.ProductRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.study.springboot.entity.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findAll(int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("itemUpdateDatetime"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<ProductEntity> list = productRepository.findAll(pageable);

        return list.map(ProductResponseDto::new);
    }

    //상품 검색

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findByKeyword(String findByType1, String findByType2, String keyword, int page) {

        Page<ProductEntity> list;
        Pageable pageable = PageRequest.of(page, 10);

        if (findByType1.equals("all") && findByType2.equals("all")) {
            list = productRepository.findByKeyword(keyword, pageable);
        } else if (!findByType1.equals("all") && findByType2.equals("all")) {
            list = productRepository.findByType1(findByType1, keyword, pageable);
        } else if(findByType1.equals("all") && !findByType2.equals("all")) {

            if (findByType2.equals("상품명")) {
                list = productRepository.findByItemName(keyword, pageable);
            } else if (findByType2.equals("상품번호")) {
                list = productRepository.findByItemNo(keyword, pageable);
            } else { //상품가격
                list = productRepository.findByItemPrice(keyword, pageable);
            }

        } else{ //(!findByType1.equals("all") && !findByType2.equals("all"))
            if (findByType2.equals("상품명")) {
                list = productRepository.findByItemName(findByType1, keyword, pageable);
            } else if (findByType2.equals("상품코드")) {
                list = productRepository.findByItemNo(findByType1, keyword, pageable);
            } else { //상품가격
                list = productRepository.findByItemPrice(findByType1, keyword, pageable);
            }
        }

        return list.map(ProductResponseDto::new);
    }


    @Transactional(readOnly = true)
    public ProductResponseDto findById(Long id) {

        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
        ProductResponseDto dto = setItemDiscountPrice(new ProductResponseDto(entity));
        return dto;
    }

    ProductResponseDto setItemDiscountPrice( ProductResponseDto dto){

        Long itemPrice = dto.getItemPrice();
        Long itemDiscountRate = dto.getItemDiscountRate();
        long price = (long) (Math.floor((itemPrice * ( (100 - itemDiscountRate) * 0.01))/100)) *100;
        dto.setItemDiscountPrice(price);

        return dto;
    }

    //삭제
    @Transactional
    public boolean productDelete(Long id){
        try {
            ProductEntity entity = productRepository.findById(id).get();
            productRepository.delete(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //선택 삭제
    @Transactional
    public boolean productDeleteCheck(String arrStr) {
        try {
            String[] arrIdx = arrStr.split(",");
            for (int i=0; i<arrIdx.length; i++) {
                ProductEntity productEntity = productRepository.findById((long) Integer.parseInt(arrIdx[i])).get();
                productRepository.delete(productEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    //수정
    @Transactional
    public boolean productModify(ProductSaveRequestDto dto){
        try {
            dto.setItemUpdateDatetime(LocalDateTime.now());
            ProductEntity entity = dto.toUpdateEntity();
            productRepository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //등록
    @Transactional
    public boolean productRegistration(ProductSaveRequestDto dto){
        try {
            dto.setItemUpdateDatetime(LocalDateTime.now());
            ProductEntity entity = dto.toSaveEntity();
            productRepository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;
    @Value("${cloud.aws.s3.bucket.name}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile file) {

        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try(InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }

        return defaultUrl + fileName;
    }

    private String createFileName(String fileName) { // 먼저 파일 업로드 시, 파일명을 난수화하기 위해 random으로 돌립니다.
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) { // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    @Transactional(readOnly = true)
    public String findByUrl(Long id) {
        ProductEntity productEntity = productRepository.findById(id).get();
        ProductResponseDto dto = new ProductResponseDto(productEntity);
        return dto.getItemImageUrl();
    }

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

    public List<ProductResponseDto> itemListByCookies(Cookie[] cookies) {
        List<ProductResponseDto> itemList = new ArrayList<>();
        for (Cookie c : cookies) {
            String name = c.getName(); // 쿠키 이름 가져오기

            if (name.startsWith("item_idx.")) {
                // 변수 선언
                Long itemNo = Long.parseLong(name.split("\\.")[1]);
                // itemList
                ProductResponseDto productResponseDto = findById(itemNo);
                itemList.add(productResponseDto);
            }
        }
        return itemList;
    }


    public List<CartResponseDto> cartListByCookies(Cookie[] cookies) {
        List<CartResponseDto> cartList = new ArrayList<>();
        for (Cookie c : cookies) {
            String name = c.getName(); // 쿠키 이름 가져오기
            String value = c.getValue(); // 쿠키 값 가져오기

            if (name.startsWith("item_idx.")) {

                // 변수 선언
                Long itemNo = Long.parseLong(name.split("\\.")[1]);
                Long cartItemAmount = Long.parseLong(value);
                String itemOptionColor = "";
                try {
                    itemOptionColor = URLDecoder.decode(name.split("\\.")[2], "UTF-8");
                }catch (Exception e) {
                    e.printStackTrace();
                }
                String itemOptionSize = name.split("\\.")[3];

                //name value

                // itemList
                ProductResponseDto productResponseDto = findById(itemNo);

                // cartList
                String cartCode = UUID.randomUUID().toString();
                Long cartDiscountPrice = productResponseDto.getItemPrice() * productResponseDto.getItemDiscountRate() / 100;
                Long cartItemPrice = (productResponseDto.getItemPrice() - cartDiscountPrice) /100 * 100;
                cartDiscountPrice = productResponseDto.getItemPrice() - cartItemPrice;

                CartResponseDto cartResponseDto = CartResponseDto.builder()
                        .cartCode(cartCode)
                        .itemCode(String.valueOf(itemNo))
                        .itemName(productResponseDto.getItemName())
                        .itemOptionColor(itemOptionColor)
                        .itemOptionSize(itemOptionSize)
                        .cartItemAmount(cartItemAmount)
                        .cartItemOriginalPrice(productResponseDto.getItemPrice())
                        .cartDiscountPrice(cartDiscountPrice)
                        .cartItemPrice(cartItemPrice)
                        .build();
                cartList.add(cartResponseDto);

            }
        }
        return cartList;
    }
}
