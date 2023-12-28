package tomato.classifier.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NeedLoginController {

    //로그인 하지 않은 사용자의 댓글 작성을 처리
    @GetMapping("/article/{articleId}/comment-add")
    public String commentNotLogin(@PathVariable Integer articleId){

        return "redirect:/article/detail/" + articleId;
    }

    //로그인 하지 않은 사용자의 추천/비추천 처리
    @GetMapping("/article/{articleId}/likeOrHate")
    public String likeOrHateNotLogin(@PathVariable Integer articleId){

        return "redirect:/article/detail/" + articleId;
    }
}


