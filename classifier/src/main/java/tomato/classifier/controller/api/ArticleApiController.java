package tomato.classifier.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tomato.classifier.dto.ArticleDto;
import tomato.classifier.dto.ArticleLikeHateDto;
import tomato.classifier.entity.Article;
import tomato.classifier.service.ArticleService;


import javax.transaction.Transactional;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleApiController {

    private final ArticleService articleService;

    @PostMapping("/add")
    @Transactional
    public ResponseEntity<ArticleDto> write(@RequestBody ArticleDto articleDTO){

        ArticleDto articleDto = articleService.save(articleDTO);

        return new ResponseEntity<>(articleDto, HttpStatus.OK);
    }


    @PatchMapping("/edit/{articleId}")
    @Transactional
    public ResponseEntity<ArticleDto> edit(@PathVariable Integer articleId , @RequestBody ArticleDto articleDTO){

        ArticleDto updateDto = articleService.update(articleId, articleDTO);

        return ResponseEntity.status(HttpStatus.OK).body(updateDto);
    }

    @Transactional
    @DeleteMapping("/delete/{articleId}")
    public ResponseEntity<Article> delete(@PathVariable Integer articleId) {

        Article deleted = articleService.delete(articleId);

        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }


    @PostMapping("/like")
    public ResponseEntity<ArticleLikeHateDto> likeHate(@RequestBody ArticleLikeHateDto articleLikeHateDto, Authentication authentication){

        articleLikeHateDto.setMemberId(authentication.getName());

        log.info("getName = {} ", authentication.getName());
        ArticleLikeHateDto result =  articleService.likeHate(articleLikeHateDto);
        log.info("onlyLike 호출 {}" ,articleLikeHateDto.getStatus());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
