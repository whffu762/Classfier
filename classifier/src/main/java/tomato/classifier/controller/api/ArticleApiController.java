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


    @PostMapping("/onlyLike")
    public ResponseEntity<ArticleLikeHateDto> onlyLike(@RequestBody ArticleLikeHateDto articleLikeHateDto, Authentication authentication){

        ArticleLikeHateDto result =  articleService.likeHate(articleLikeHateDto, authentication, "onlyLike");
        log.info("onlyLike 호출 {}" ,articleLikeHateDto.getLikeHate());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @PostMapping("/onlyHate")
    public ResponseEntity<ArticleLikeHateDto> onlyHate(@RequestBody ArticleLikeHateDto articleLikeHateDto, Authentication authentication){

        ArticleLikeHateDto result =  articleService.likeHate(articleLikeHateDto, authentication, "onlyHate");
        log.info("onlyHate 호출 {}" ,articleLikeHateDto.getLikeHate());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/unLikeAndHate")
    public ResponseEntity<ArticleLikeHateDto> unLikeAndHate(@RequestBody ArticleLikeHateDto articleLikeHateDto, Authentication authentication){

        ArticleLikeHateDto result =  articleService.likeHate(articleLikeHateDto, authentication, "unLikeAndHate");
        log.info("unLikeAndHate 호출 {}",articleLikeHateDto.getLikeHate());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/unHateAndLike")
    public ResponseEntity<ArticleLikeHateDto> unHateAndLike(@RequestBody ArticleLikeHateDto articleLikeHateDto, Authentication authentication){

        ArticleLikeHateDto result =  articleService.likeHate(articleLikeHateDto, authentication, "unHateAndLike");
        log.info("unHateAndLike 호출 {}" ,articleLikeHateDto.getLikeHate());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
