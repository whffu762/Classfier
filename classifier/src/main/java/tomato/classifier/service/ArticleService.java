package tomato.classifier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tomato.classifier.dto.ArticleDto;
import tomato.classifier.dto.ArticleLikeHateDto;
import tomato.classifier.dto.LikeHateDto;
import tomato.classifier.entity.Article;
import tomato.classifier.entity.ArticleLikeHate;
import tomato.classifier.entity.Member;
import tomato.classifier.repository.article.queryDsl.ArticleLikeHateCustomRepository;
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
        articleDTO.setLikeNum(0);
        articleDTO.setHateNum(0);
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


    public ArticleLikeHateDto likeHate(ArticleLikeHateDto input){

        //input
        Member requestMember = memberRepository.findById(input.getMemberId());

        //기존 좋아요 싫어요 갯수
        Article articleEntity = articleRepository.findById(input.getArticleId())
                .orElseThrow(()-> new IllegalArgumentException("not Exist article"));
        ArticleDto beforeArticleDto = ArticleDto.convertDto(articleEntity);
        Integer oldLikeNum = beforeArticleDto.getLikeNum();
        Integer oldHateNum = beforeArticleDto.getHateNum();

        //기존에 DB 에 저장되어있는 좋아요 싫어요
        ArticleLikeHateDto savedArticleLikeHateDto = checkExistLikeHate(input);

        Boolean requestStatus = input.getStatus();


        if(savedArticleLikeHateDto == null){
            if(requestStatus){
                beforeArticleDto.setLikeNum(++oldLikeNum);
            }
            else{
                beforeArticleDto.setHateNum(++oldHateNum);
            }
        }
        else{
            Boolean savedStatus = savedArticleLikeHateDto.getStatus();
            if(requestStatus){
                if(savedStatus == null){
                    beforeArticleDto.setLikeNum(++oldLikeNum);
                }
                else if(savedStatus){
                    beforeArticleDto.setLikeNum(--oldLikeNum);
                    input.setStatus(null);
                }
                else{
                    beforeArticleDto.setLikeNum(++oldLikeNum);
                    beforeArticleDto.setHateNum(--oldHateNum);
                }
            }
            else{
                if(savedStatus == null){
                    beforeArticleDto.setHateNum(++oldHateNum);
                }
                else if(savedStatus){
                    beforeArticleDto.setLikeNum(--oldLikeNum);
                    beforeArticleDto.setHateNum(++oldHateNum);
                }
                else{
                    beforeArticleDto.setHateNum(--oldHateNum);
                    input.setStatus(null);
                }
            }
            input.setId(savedArticleLikeHateDto.getId());
        }

        Article afterArticle = Article.convertEntity(beforeArticleDto);
        articleRepository.save(afterArticle);
        articleLikeHateRepository.save(ArticleLikeHate.convertEntity(input, requestMember,afterArticle));

        return input;
    }

    public ArticleLikeHateDto checkExistLikeHate(ArticleLikeHateDto input){

        //DB 에 저장된 값 있는지 확인
        ArticleLikeHate savedArticleLikeHate = articleLikeHateCustomRepository.findLikeHate(input.getMemberId(), input.getArticleId());
        if(savedArticleLikeHate == null){
            return null;
        }

        return ArticleLikeHateDto.convertDto(savedArticleLikeHate);
    }

    public ArticleLikeHateDto getArticleLikeHateInfo(List<ArticleLikeHate> likeHates, Integer articleId){

        ArticleLikeHateDto result = new ArticleLikeHateDto();

        for(ArticleLikeHate likeHate : likeHates){
            if(likeHate.getArticle().getArticleId().equals(articleId)){
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

