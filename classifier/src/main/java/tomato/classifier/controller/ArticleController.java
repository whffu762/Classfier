package tomato.classifier.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tomato.classifier.dto.ArticleDto;
import tomato.classifier.dto.CommentDto;
import tomato.classifier.dto.MemberDto;
import tomato.classifier.service.ArticleService;
import tomato.classifier.service.CommentService;
import tomato.classifier.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;

    private final MemberService memberService;

    //R
    @GetMapping
    public String articleMain(Model model){

        List<ArticleDto> articleDtos = articleService.showAll();

        model.addAttribute("articles", articleDtos);

        return "board/articleMain";
    }

    //C
    @GetMapping("/add")
    public String articleAdd(Authentication authentication, Model model) {

        MemberDto authenticationUser = memberService.findAuthenticatedUserInfo(authentication);

        model.addAttribute("nickName", authenticationUser.getNickname());

        return "board/articleAdd";
    }

    //U1
    @GetMapping("/detail/{articleId}")
    public String articleDetail(@PathVariable Integer articleId,Authentication authentication ,Model model) {

        ArticleDto articleDto = articleService.show(articleId);
        List<CommentDto> comments = commentService.comments(articleId);
        if(authentication != null){
            MemberDto authenticationUser = memberService.findAuthenticatedUserInfo(authentication);
            model.addAttribute("nickName", authenticationUser.getNickname());
        }

        model.addAttribute("article", articleDto);
        model.addAttribute("comments", comments);

        return "board/articleDetail";
    }

    //U2
    @GetMapping("/edit")
    public String articleUpdate(@RequestParam Integer articleId, Model model){

        ArticleDto articleDto = articleService.show(articleId);

        model.addAttribute("article", articleDto);

        return "board/articleUpdate";
    }

}
