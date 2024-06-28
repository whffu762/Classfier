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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleApiController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleDto> write(@RequestBody ArticleDto articleDTO){

        ArticleDto article = articleService.save(articleDTO);

        return new ResponseEntity<>(article, HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<ArticleDto> edit(@RequestBody ArticleDto articleDTO){

        ArticleDto updated = articleService.update(articleDTO);

        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping
    public ResponseEntity<ArticleDto> delete(@RequestParam("no") Integer articleId) {

        ArticleDto deleted = articleService.delete(articleId);

        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }

    @PostMapping("/like-hate")
    public ResponseEntity<ArticleLikeHateDto> likeHate(@RequestBody ArticleLikeHateDto articleLikeHateDto, Authentication authentication){

        articleLikeHateDto.setMemberId(authentication.getName());
        ArticleLikeHateDto result =  articleService.likeHate(articleLikeHateDto);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
