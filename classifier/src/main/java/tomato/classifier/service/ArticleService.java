package tomato.classifier.service;

import lombok.NoArgsConstructor;
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
import tomato.classifier.entity.Comment;
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
    private final CommentService commentService;

    public List<ArticleDto> showAll(){

        List<Article> articles = articleRepository.findAll(Sort.by(Sort.Direction.DESC, "articleId"));
        List<ArticleDto> articleDtoList = new ArrayList<>();

        for(Article article : articles){

            if(!article.isDeleteYn()){
                ArticleDto articleDTO = article.convertDto();
                articleDtoList.add(articleDTO);
            }
        }

        return articleDtoList;
    }


    public ArticleDto show(Integer articleId){

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("id err"));

        return article.convertDto();
    }

    public ArticleDto save(ArticleDto articleDTO){

        articleDTO.setDeleteYn(false);
        articleDTO.setUpdateYn(false);
        articleDTO.setLikeNum(0);
        articleDTO.setHateNum(0);
        Article article= articleDTO.convertEntity();
        articleRepository.save(article);

        return articleDTO;
    }

    public ArticleDto update(ArticleDto inputArticleDto){

        //이런 예외 처리도 뭔가 해야될 거 같은데
        ArticleDto newArticleDto = articleRepository.findById(inputArticleDto.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("article update - 게시글 조회 실패"))
                .convertDto();

        //Bean Validation 방식으로 업데이트 예정
        if(!inputArticleDto.getTitle().isEmpty() && !inputArticleDto.getContent().isEmpty()){
            newArticleDto.setTitle(inputArticleDto.getTitle());
            newArticleDto.setContent(inputArticleDto.getContent());
            newArticleDto.setUpdateYn(true);
        }

        return articleRepository.save(newArticleDto.convertEntity()).convertDto();
    }

    public ArticleDto delete(Integer articleId) {

        ArticleDto oldArticleDto = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("article delete - 게시글 조회 실패"))
                .convertDto();

        oldArticleDto.setDeleteYn(true);

        for(Comment commentOfOldArticle : oldArticleDto.getComments()){
            commentService.delete(commentOfOldArticle.convertDto());
        }

        return articleRepository.save(oldArticleDto.convertEntity()).convertDto();
    }

    public ArticleLikeHateDto likeHate(ArticleLikeHateDto input){

        //input
        Member requestMember = memberRepository.findById(input.getMemberId());

        //기존 좋아요 싫어요 갯수
        Article article = articleRepository.findById(input.getArticleId())
                .orElseThrow(()-> new IllegalArgumentException("not Exist article"));
        ArticleDto beforeArticleDto = article.convertDto();
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

        Article afterArticle = beforeArticleDto.convertEntity();
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
}

