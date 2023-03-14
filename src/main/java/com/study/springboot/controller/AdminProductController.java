package com.study.springboot.controller;

import com.study.springboot.dto.product.FileResponse;
import com.study.springboot.dto.product.ProductResponseDto;
import com.study.springboot.dto.product.ProductSaveRequestDto;
import com.study.springboot.repository.CartRepository;
import com.study.springboot.repository.NoticeRepository;
import com.study.springboot.repository.OrderRepository;
import com.study.springboot.repository.ProductRepository;
import com.study.springboot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class AdminProductController {
    final ProductService productService;
    final OrderService orderService;
    final CartService cartService;
    final ProductRepository productRepository;
    final OrderRepository orderRepository;
    final AwsS3Service awsS3Service;
    final Service3 service3;
    final CartRepository cartRepository;
    final ReviewService reviewService;
    final Service5 service5;
    final NoticeRepository noticeRepository;

    @GetMapping("/admin/product")
    public String productHome(){
        return "redirect:/admin/product/list?page=0";
    }

    //상품리스트
    @GetMapping("/admin/product/list")
    public String productList(Model model,
                              @RequestParam(value = "findByType1", required = false) String findByType1,
                              @RequestParam(value = "findByType2", required = false) String findByType2,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "page", defaultValue = "0") int page) {

        Page<ProductResponseDto> list;
        int totalPage;
        List<Integer> pageList;

        if ((findByType1 == null) && (findByType2 == null) && (keyword == null)
                || (findByType1.equals("null")) && (findByType2.equals("null")) && (keyword.equals("null"))
                || (findByType1.equals("all")) && (findByType2.equals("all")) && (keyword.equals(""))) {
            list = productService.findAll(page);
        } else {
            list = productService.findByKeyword(findByType1, findByType2, keyword, page);
        }
        totalPage = list.getTotalPages();
        pageList = productService.getPageList(totalPage, page);
        model.addAttribute("list", list);
        model.addAttribute("findByType1", findByType1);
        model.addAttribute("findByType2", findByType2);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageList", pageList);
        //상품 개수
        long listCount = productRepository.count();
        model.addAttribute("listCount", listCount);

        return "/admin/product/list";
    }

    //상품등록 폼
    @GetMapping("/admin/product/registration")
    public String productRegistration() {
        return "/admin/product/registration";
    }


    //상품 단건조회
    //http://localhost:8080/admin/product/content?item_no=1
    @GetMapping("/admin/product/content")
    public String productContent(@RequestParam(value = "itemNo") long id, Model model) {
        ProductResponseDto dto = productService.findById(id);
        model.addAttribute("dto", dto);

        return "admin/product/modify";
    }

    //상품삭제
    //http://localhost:8080/admin/product/delete?item_no=20000
    @ResponseBody
    @GetMapping("/admin/product/delete")
    public String productDelete(@RequestParam(value = "itemNo") long id) {
        boolean result = productService.productDelete(id);
        if (!result) {
            return "<script>alert('삭제 실패');location.href='/admin/product/list/';</script>";
        }
        return "<script>alert('삭제 완료');location.href='/admin/product/list/';</script>";
    }

    //상품 선택삭제
    @ResponseBody
    @RequestMapping("/admin/product/delete/check")
    public String delete (@RequestParam("itemNo") String arrStr){
        boolean result = productService.productDeleteCheck(arrStr);
        if (!result) {
            return "<script>alert('선택삭제 실패');location.href='/admin/product/list/';</script>";
        }
        return "<script>alert('선택삭제 완료');location.href='/admin/product/list/';</script>";
    }


    //상품수정
    @ResponseBody
    @RequestMapping("/find/admin/product/modify/action")
    public String productModify(@RequestParam MultipartFile uploadfile, ProductSaveRequestDto dto) {
        String url;
        if (uploadfile.getOriginalFilename().equals("")) {
            url = productService.findByUrl(dto.getItemNo());
        } else {
            url = awsS3Service.upload(uploadfile);
            new ResponseEntity<>(FileResponse.builder().
                    uploaded(true).
                    url(url).
                    build(), HttpStatus.OK);
        }
        dto.setItemImageUrl(url);

        boolean result = productService.productModify(dto);
        if (!result) {
            return "<script>alert('수정 실패');location.href='/admin/product/list/';</script>";
        }
        return "<script>alert('수정 완료');location.href='/admin/product/list/';</script>";
    }

    //리스트에서 수정
    @ResponseBody
    @RequestMapping("/admin/product/list/modify/action")
    public String productModify(ProductSaveRequestDto dto) {

        boolean result = productService.productModify(dto);
        if (!result) {
            return "<script>alert('수정 실패');location.href='/admin/product/list/';</script>";
        }
        return "<script>alert('수정 완료');location.href='/admin/product/list/';</script>";
    }


    //상품등록
    @ResponseBody
    @RequestMapping("/find/admin/product/registration/action")
    public String productRegistrationAction(@RequestParam MultipartFile uploadfile, ProductSaveRequestDto dto)
            throws IllegalStateException, IOException {

        String url = awsS3Service.upload(uploadfile);

        new ResponseEntity<>(FileResponse.builder().
                uploaded(true).
                url(url).
                build(), HttpStatus.OK);

        dto.setItemImageUrl(url);

        boolean result = productService.productRegistration(dto);
        if (!result) {
            return "<script>alert('등록 실패');location.href='/admin/product/list/';</script>";
        }
        return "<script>alert('등록 완료');location.href='/admin/product/list/';</script>";

    }

    //aws 이미지 업로드
    @PostMapping("find/admin/imgUpload")
    @ResponseBody
    public ResponseEntity<FileResponse> imgUpload(
            @RequestPart(value = "upload", required = false) MultipartFile fileload) throws Exception {

        return new ResponseEntity<>(FileResponse.builder().
                uploaded(true).
                url(productService.upload(fileload)).
                build(), HttpStatus.OK);
    }
}
