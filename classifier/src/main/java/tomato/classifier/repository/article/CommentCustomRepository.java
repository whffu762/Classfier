package tomato.classifier.repository.article;

import tomato.classifier.entity.Comment;

import java.util.List;

public interface CommentCustomRepository {

    List<Comment> findByArticleId(Integer articleId);

}
