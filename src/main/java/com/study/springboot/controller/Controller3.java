package com.study.springboot.controller;

import com.study.springboot.dto.review.ReviewSaveDto;
import com.study.springboot.entity.review.Review;
import com.study.springboot.repository.ReviewRopository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class Controller3 {
    //private final ReviewService reviewService;
    private final ReviewRopository reviewRopository;
    //url:http://localhost:8080/review/
    @RequestMapping("/")
    public String main(){
        return "redirect:/review/listForm";
    }
    @RequestMapping("/listForm")
    public String list(Model model){
        //정렬해서 검색하면 되는지 보기
        List <Review> list = reviewRopository.findAll();
        model.addAttribute("list",list);
        model.addAttribute("listcount",list.size());
        return "/admin/review/test";
    }
    @RequestMapping("/modify")
    public String modify(@RequestParam("reviewNo") int no,
                         Model model){
        Optional<Review> optional = reviewRopository.findById((long)no);
        Review review = optional.get();
        model.addAttribute("review",review);
        return "/admin/review/review2";
    }

    @RequestMapping("modifyAction")
    @ResponseBody
    public String modifyAction(ReviewSaveDto dto) {
        try{
            Review review = dto.toUpdateEntity();
            reviewRopository.save(review);
        }catch (Exception e){
            e.printStackTrace();
            return "<script>alert('리뷰수정 실패');history.back();</script>";
        }
        return "<script>alert('리뷰수정 성공');location.href='/review/listForm';</script>";
    }
//    throws Exception
//    return "/admin/review/review2";
    @RequestMapping("search")
    public String search(@RequestParam("reviewNo") String reviewNo){
        //findByName
        //findByMemberId
        return "/admin/review/review2";

    }
    //선택수정
    @RequestMapping("/update")
    public String update (@RequestParam("reviewNo") String reviewNo,
                          Model model){
        System.out.println(reviewNo);
        ArrayList <Review> list = new ArrayList<>();
        String[] arrIdx = reviewNo.split(",");
        for (int i=0; i<arrIdx.length; i++) {
            //수정할 목록 찾아서 목록 만듬
            Optional<Review> optional = reviewRopository.findById((long)Integer.parseInt(arrIdx[i]));
            list.add(optional.get());
            //ArrayList <Review> list1 = new ArrayList<>();
            //Optional<Review> optional = reviewRopository.findById((long)Integer.parseInt(arrIdx[i]));
            //list1.add(optional.get());
        }
        System.out.println("작업끝");
        model.addAttribute("list",list);
        model.addAttribute("listcount",list.size());
        return "/admin/review/review3" ;
    }
    //선택삭제하기
    @RequestMapping("/delete")
    public String delete (@RequestParam("reviewNo") String reviewNo){
        System.out.println(reviewNo);
        String[] arrIdx = reviewNo.split(",");
        for (int i=0; i<arrIdx.length; i++) {
            Optional<Review> optional = reviewRopository.findById((long)Integer.parseInt(arrIdx[i]));
            reviewRopository.delete(optional.get());
        }

        return "redirect:/review/listForm";
    }

}
//@Controller("CheckboxController.class")
//@RequestMapping(value="/checkbox")
//public class CheckboxController {
//
//    private static final Logger log = LoggerFactory.getLogger(CheckboxController.class);
//
//    @RequestMapping(value="/view")
//    public String view(HttpServletRequest request, SampleVO vo) throws Exception {
//        return "/checkbox/view";
//    }
//
//    @RequestMapping(value="/updateChkBox")
//    public @ResponseBody String updateChkBox (
//            HttpServletRequest request,
//            HttpServletResponse response,
//            @RequestParam(value="name",required=true) List<String> name,
//            @RequestParam(value="age",required=true) List<Integer> age) throws Exception {
//
//        log.debug( ">>> param size : " + name.size() );
//
//        int i = 0;
//        for( String value : name ){
//            log.debug( ">>> name's value : " + value + "\tage : " + age.get(i) );
//            i++;
//        }
//
//        return "success";
//    }
//}