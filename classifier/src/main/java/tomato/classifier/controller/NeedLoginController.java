package tomato.classifier.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NeedLoginController {

    //로그인 하지 않은 사용자의 댓글 작성을 처리
    @GetMapping("/comment")
    public String commentNotLogin(@RequestParam("no") Integer articleId){

        return "redirect:/article?no=" + articleId;
    }

    //로그인 하지 않은 사용자의 추천/비추천 처리
    @GetMapping("/article/like-hate")
    public String likeOrHateNotLogin(@RequestParam("no") Integer articleId){

        return "redirect:/article?no=" + articleId;
    }
}


