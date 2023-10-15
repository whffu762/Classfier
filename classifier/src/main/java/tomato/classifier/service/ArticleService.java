package tomato.classifier.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tomato.classifier.dto.ArticleDto;
import tomato.classifier.dto.ArticleLikeHateDto;
import tomato.classifier.entity.Article;
import tomato.classifier.entity.ArticleLikeHate;
import tomato.classifier.entity.Member;
import tomato.classifier.repository.article.ArticleLikeHateCustomRepository;
import tomato.classifier.repository.article.ArticleLikeHateRepository;
import tomato.classifier.repository.article.ArticleRepository;
import tomato.classifier.repository.auth.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ArticleLikeHateRepository articleLikeHateRepository;
    private final ArticleLikeHateCustomRepository articleLikeHateCustomRepository;

    public List<ArticleDto> showAll(){

        List<Article> articles = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "articleId"));
        List<ArticleDto> articleDtoList = new ArrayList<>();

        for(Article article : articles){

            if(!article.isDeleteYn()){
                ArticleDto articleDTO = ArticleDto.convertDto(article);
                articleDtoList.add(articleDTO);
            }
        }

        return articleDtoList;
    }


    public ArticleDto show(Integer articleId){

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("id err"));

        return ArticleDto.convertDto(article);
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

        //dto - update - entity - dto
        Article updated = articleRepository.save(target);

        return ArticleDto.convertDto(updated);
    }

    public Article delete(Integer articleId) {
        Article target = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("id err"));
        target.delete();

        return articleRepository.save(target);

    }

        /*
    likeHate 값
    true - like
    false - hate
    null - nothing
     */


    public ArticleLikeHateDto likeHate(ArticleLikeHateDto input, Authentication authentication, String controller){

        //input
        Member member = memberRepository.findById(authentication.getName());
        Integer articleId = input.getArticleId();
        Boolean likeHate = input.getLikeHate();

        //DB에 저장된 값
        ArticleLikeHateDto saved = checkExist(authentication.getName(), articleId);

        //좋아요 싫어요 갯수
        Article articleEntity = articleRepository.findById(articleId)
                .orElseThrow(()-> new IllegalArgumentException("not Exist articleId"));
        ArticleDto article = ArticleDto.convertDto(articleEntity);
        Integer likeNum = article.getLikeNum();
        Integer hateNum = article.getHateNum();


        switch (controller){
            case "onlyLike" :
                input = onlyLike(member, article, saved, input, likeHate, likeNum);
                break;
            case "onlyHate" :
                input = onlyHate(member, article, saved, input, likeHate, hateNum);
                break;
            case "unLikeAndHate" :
                input = unLikeAndHate(member, article, saved, input, likeNum, hateNum);
                break;
            case "unHateAndLike" :
                input = unHateAndLike(member, article, saved, input, likeNum, hateNum);
                break;
        }

        return input;
    }

    public ArticleLikeHateDto checkExist(String memberId, Integer articleId){
        //DB 에 이미 저장된 적이 있는지 확인
        ArticleLikeHate tmp = articleLikeHateCustomRepository.findLikeHate(memberId, articleId);

        if(tmp == null){
            return null;
        }

        return ArticleLikeHateDto.convertDto(tmp);
    }


    public ArticleLikeHateDto checkSavedNull(ArticleLikeHateDto saved, ArticleLikeHateDto input ,Boolean likeHate){
        //Null 인지 확인

        if(saved != null){
            input = saved;
        }

        input.setLikeHate(likeHate);

        return input;
    }

    public ArticleLikeHateDto onlyLike(Member member, ArticleDto article, ArticleLikeHateDto saved, ArticleLikeHateDto input ,
                                       Boolean likeHate, Integer likeNum){
        //갯수 count, likeHate 변환
        if(likeHate){
            article.setLikeNum(++likeNum);
        }
        else{
            article.setLikeNum(--likeNum);
            likeHate = null;
        }

        input = checkSavedNull(saved, input, likeHate);

        articleLikeHateRepository.save(ArticleLikeHate.convertEntity(input, member));
        articleRepository.save(Article.convertEntity(article));

        return input;

    }

    public ArticleLikeHateDto unLikeAndHate(Member member, ArticleDto article, ArticleLikeHateDto saved, ArticleLikeHateDto input,
                                            Integer likeNum, Integer hateNum){
        //갯수 count, likeHate 변환
        article.setLikeNum(--likeNum);
        article.setHateNum(++hateNum);
        boolean likeHate = false;

        input = checkSavedNull(saved, input, likeHate);

        articleLikeHateRepository.save(ArticleLikeHate.convertEntity(input, member));
        articleRepository.save(Article.convertEntity(article));

        return input;
    }

    public ArticleLikeHateDto onlyHate(Member member, ArticleDto article, ArticleLikeHateDto saved, ArticleLikeHateDto input,
                                            Boolean likeHate, Integer hateNum){
        //갯수 count, likeHate 변환
        if(likeHate){
            article.setHateNum(++hateNum);
            likeHate = false;
        }
        else{
            article.setHateNum(--hateNum);
            likeHate = null;
        }

        input = checkSavedNull(saved, input, likeHate);

        articleLikeHateRepository.save(ArticleLikeHate.convertEntity(input, member));
        articleRepository.save(Article.convertEntity(article));

        return input;
    }

    public ArticleLikeHateDto unHateAndLike(Member member, ArticleDto article, ArticleLikeHateDto saved, ArticleLikeHateDto input,
                                            Integer likeNum, Integer hateNum){

        //갯수 count, likeHate 변환
        article.setHateNum(--hateNum);
        article.setLikeNum(++likeNum);
        boolean likeHate = true;

        input = checkSavedNull(saved, input, likeHate);

        articleLikeHateRepository.save(ArticleLikeHate.convertEntity(input, member));
        articleRepository.save(Article.convertEntity(article));

        return input;

    }


    public ArticleLikeHateDto tmpFunc(List<ArticleLikeHate> list, Integer articleId){

        ArticleLikeHateDto result = new ArticleLikeHateDto();
        for(ArticleLikeHate likeHate : list){
            if(likeHate.getArticleId().equals(articleId)){
                result = ArticleLikeHateDto.convertDto(likeHate);
            }
        }
        //Stream 으로 바꾸는건 나중에 하자구..

        return result;
    }




    /*
    이전 방식
    public ArticleLikeHateDto checkLike(ArticleLikeHateDto articleLikeHateDto){

        Member member = memberRepository.findById(articleLikeHateDto.getMemberId());
        Integer articleId = articleLikeHateDto.getArticleId();

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 조회 실패"));
        ArticleDto articleDto = ArticleDto.convertDto(article);

        int likeNum = articleDto.getLikeNum();
        int hateNum = articleDto.getHateNum();

        ArticleLikeHateDto input = articleLikeHateDto;
        input.setDeleteYn(false);


        ArticleLikeHate tmp = articleLikeHateCustomRepository.findLikeHate(member, articleId);
        if(articleLikeHateDto.getLike() == null){
            input = deleteLikeHate(articleLikeHateDto);
        }
        else{

            if(tmp != null) {
                ArticleLikeHateDto savedDto = ArticleLikeHateDto.convertDto(tmp);
                input = updateLikeHate(articleLikeHateDto, savedDto, member);


            }
        }

        ArticleLikeHate entity = ArticleLikeHate.convertEntity(input, member, input.getDeleteYn());
        articleLikeHateRepository.save(entity);

        return input;
    }




    public ArticleLikeHateDto updateLikeHate(ArticleLikeHateDto input, ArticleLikeHateDto saved, Member member){

        boolean target = input.getLike();
        boolean savedLike = saved.getLike();

        if(saved.getDeleteYn()){
            if(target ^ savedLike){ //둘이 다르면
                saved.setLike(target);
            }
            saved.setDeleteYn(false);
        }
        else{
            if(target ^ savedLike) { //둘이 다르면
                saved.setLike(target);
            }
        }
        return saved;
    }

    public ArticleLikeHateDto deleteLikeHate(ArticleLikeHateDto input){

        input.setDeleteYn(true);

        return input;
    }*/

}

