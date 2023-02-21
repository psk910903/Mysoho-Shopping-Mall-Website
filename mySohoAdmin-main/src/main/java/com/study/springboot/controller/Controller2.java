package com.study.springboot.controller;

import com.study.springboot.dto.notice.NoticeResponseDto;
import com.study.springboot.dto.notice.NoticeSaveRequestDto;
import com.study.springboot.dto.notice.NoticeUpdateRequestDto;
import com.study.springboot.object.FileResponse;
import com.study.springboot.repository.NoticeRepository;
import com.study.springboot.service.AwsS3Service;
import com.study.springboot.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/notice")
public class Controller2 {

    private final NoticeService noticeService;
    private final AwsS3Service awsS3Service;
    private final NoticeRepository noticeRepository;


    // URL: localhost8080:/admin/notice/
    @GetMapping("/")
    public String noticeHome(){
        return "redirect:/admin/notice/list?page=0"; // localhost8080:/admin/notice/list로 redirect
    }

    // URL: localhost8080:/admin/notice/list
    // 공지글 리스트 페이지
    @GetMapping("/list")
    public String noticeList(@RequestParam(value = "findBy", required = false) String findBy,   // findBy : type(종류), title(제목), content(내용)에 따라 글 분류
                       @RequestParam(value = "keyword", required = false) String keyword, // keyword: 어떤 keyword로 찾을 것인지 결정
                       @RequestParam(value = "page", defaultValue = "0") int page,        // page: 0에서부터 시작
                        Model model) {                                                    // ex) findBy=title, keyword="키워드입니다", page:2면
                                                                                          //     제목에서 "키워드입니다"가 포함된 글 중 3쪽을 보여줌
        Page<NoticeResponseDto> list;
        if (findBy == null) { // 검색 기능을 쓰지 않을 때
            list = noticeService.findAll(page);
        } else{ // 검색 기능을 쓸 때
            list = noticeService.findByKeyword(findBy, keyword, page);
        }

        int totalPage = list.getTotalPages(); // 전체 페이지 개수
        List<Integer> pageList = noticeService.getPageList(totalPage, page); // 해당 page에서 아래쪽 페이지바에 보이는 숫자 list

        model.addAttribute("list", list);
        model.addAttribute("findBy", findBy);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageList", pageList);

        long listCount = noticeRepository.count();
        model.addAttribute("listCount", listCount);

        return "admin/notice/list"; //listForm.html로 응답

    }

    // URL: localhost8080:/admin/notice/content/{정수}
    // 상세 페이지
    @GetMapping("/content/{noticeNo}")
    public String noticeContent(@PathVariable("noticeNo") Long noticeNo, Model model) {

        NoticeResponseDto dto = noticeService.findById(noticeNo);
        if (dto == null){
            return "redirect:/admin/notice/list";
        }

        model.addAttribute("notice", dto);

        return "admin/notice/content"; //content.html로 응답
    }

    // URL: localhost8080:/admin/notice/write
    // 새 글 작성 페이지
    @GetMapping("/write")
    public String noticeWrite(Model model) {

        NoticeResponseDto dto = NoticeResponseDto.builder()
                .noticeType("공지사항")
                .noticeTitle("")
                .noticeContent("")
                .build();

        model.addAttribute("notice",dto);

        return "admin/notice/write";//write.html로 응답
    }

    // URL: localhost8080:/admin/notice/modify/{정수}
    // 기존 글 수정 페이지
    @GetMapping("/modify")
    public String noticeModify(@RequestParam("noticeNo") Long noticeNo, Model model) {

        NoticeResponseDto dto = noticeService.findById(noticeNo);
        if (dto == null){
            return "redirect:/admin/notice/list";
        }

        model.addAttribute("notice",dto);

        return "admin/notice/write"; //write.html로 응답
    }

    // 수정된 기존 글 데이터 데이터베이스에 넣기
    @PostMapping("/modifyAction")
    @ResponseBody
    public String noticeModifyAction(NoticeUpdateRequestDto dto) {

        Boolean success = noticeService.update(dto);
        if(success) {
            return "<script>alert('게시글 수정 완료'); location.href='/admin/notice/content/" + dto.getNoticeNo() + "';</script>";
        }else{
            return "<script>alert('게시글 수정 실패'); history.back();</script>";
        }
    }

    // 작성된 새로운 글 데이터 데이터베이스에 넣기
    @PostMapping("/writeAction")
    @ResponseBody
    public String noticeWriteAction(NoticeSaveRequestDto dto) {

        Boolean success = noticeService.save(dto);
        if(success) {
            return "<script>alert('게시글 등록 완료'); location.href='/admin/notice/list';</script>";
        }else{
            return "<script>alert('게시글 등록 실패'); history.back();</script>";
        }
    }

    // 기존 글 데이터베이스에서 삭제하기
    @GetMapping("/deleteAction")
    @ResponseBody
    public String noticeDeleteAction(@RequestParam("noticeNo") Long noticeNo) {

        Boolean success = noticeService.delete(noticeNo);
        if(success) {
            return "<script>alert('게시글 삭제 완료'); location.href='/admin/notice/list';</script>";
        }else{
            return "<script>alert('게시글 삭제 실패'); history.back();</script>";
        }

    }

    // 글 작성 시 이미지 업로드 할 때 awsS3에 이미지를 넣고 이미지 url 반환 (ckeditor로 이동)
    @PostMapping("/imgUpload")
    @ResponseBody
    public ResponseEntity<FileResponse> noticeImgUpload(
            @RequestPart(value = "upload", required = false) MultipartFile fileload) throws Exception {

        return new ResponseEntity<>(FileResponse.builder().
                uploaded(true).
                url(awsS3Service.upload(fileload)).
                build(), HttpStatus.OK);
    }

}
