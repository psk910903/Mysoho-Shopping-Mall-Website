package com.study.springboot.service;

import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.product.ProductSaveRequestDto;
import com.study.springboot.entity.ProductEntity;
import com.study.springboot.repository.ProductRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> findAll(int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("itemUpdateDatetime"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<ProductEntity> list = productRepository.findAll(pageable);

        return list.map(ProductResponseDto::new);
    }


    //페이징
    public List<Integer> getPageList(final int totalPage, final int page) {

        List<Integer> pageList = new ArrayList<>();

        if (totalPage <= 5){
            for (Integer i=0; i<=totalPage-1; i++){
                pageList.add(i);
            }
        }else if(page >= 0 && page <= 2){
            for (Integer i=0; i<=4; i++){
                pageList.add(i);
            }
        }
        else if (page >= totalPage-3 && page <= totalPage-1){
            for (Integer i=5; i>=1; i--){
                pageList.add(totalPage - i);
            }
        }else{
            for (Integer i=-2; i<=2; i++){
                pageList.add(page + i);
            }
        }

        return pageList;
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
        return new ProductResponseDto(entity);

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
}
