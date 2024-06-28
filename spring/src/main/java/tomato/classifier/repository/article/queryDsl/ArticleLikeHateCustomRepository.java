package tomato.classifier.repository.article.queryDsl;

import tomato.classifier.entity.ArticleLikeHate;

public interface ArticleLikeHateCustomRepository {

    ArticleLikeHate findLikeHate(String memberId, Integer articleId);
}
