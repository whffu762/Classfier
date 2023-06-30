package tomato.classifier.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tomato.classifier.dto.ArticleDto;
import tomato.classifier.entity.Article;
import tomato.classifier.service.ArticleService;


import javax.transaction.Transactional;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
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
}
