package tomato.classifier.repository.article.queryDsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import tomato.classifier.entity.ArticleLikeHate;

import static tomato.classifier.entity.QArticleLikeHate.articleLikeHate;


@Repository
public class ArticleLikeHateCustomRepositoryImpl implements ArticleLikeHateCustomRepository{

    private final JPAQueryFactory queryFactory;

    public ArticleLikeHateCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory){
        this.queryFactory = jpaQueryFactory;
    }

    public ArticleLikeHate findLikeHate(String memberId, Integer articleId){
        return queryFactory
                .select(articleLikeHate)
                .from(articleLikeHate)
                .where(articleLikeHate.article.articleId.eq(articleId), articleLikeHate.member.memberId.eq(memberId))
                .fetchOne();
    }




}
