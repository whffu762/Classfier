package tomato.classifier.repository.article;

import org.springframework.data.jpa.repository.JpaRepository;
import tomato.classifier.entity.Comment;
import tomato.classifier.repository.article.CommentCustomRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer>, CommentCustomRepository {
}
