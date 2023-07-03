package tomato.classifier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tomato.classifier.dto.ArticleDto;
import tomato.classifier.entity.Article;
import tomato.classifier.repository.article.ArticleRepository;
import tomato.classifier.repository.article.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public List<ArticleDto> showAll(){

        List<Article> articles = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "articleId"));
        List<ArticleDto> articleDtos = new ArrayList<>();

        for(Article article : articles){

            if(!article.isDeleteYn()){
                ArticleDto articleDTO = ArticleDto.convertDto(article);
                articleDtos.add(articleDTO);
            }
        }

        return articleDtos;
    }


    public ArticleDto show(Integer articleId){

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("id err"));

        ArticleDto articleDto = ArticleDto.convertDto(article);

        return articleDto;
    }

    public ArticleDto save(ArticleDto articleDTO){

        articleDTO.setDeleteYn(false);
        articleDTO.setUpdateYn(false);
        Article article= Article.convertEntity(articleDTO);
        articleRepository.save(article);

        return articleDTO;
    }

    public ArticleDto update(Integer articleId, ArticleDto articleDto){

        Article target= articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 조회 실패"));

        target.patch(articleDto);

        //dto - update - entitiy - dto
        Article updated = articleRepository.save(target);
        ArticleDto updateDto = ArticleDto.convertDto(updated);

        return updateDto;
    }

    public Article delete(Integer articleId){
        Article target = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("id err"));
        target.delete();

        Article deleted = articleRepository.save(target);

        return deleted;
    }


}

