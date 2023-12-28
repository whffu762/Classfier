package tomato.classifier.repository.article.queryDsl;

import tomato.classifier.entity.Comment;

import java.util.List;

public interface CommentCustomRepository {

    List<Comment> findByArticleId(Integer articleId);

}
