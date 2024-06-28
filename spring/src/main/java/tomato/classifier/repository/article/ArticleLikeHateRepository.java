package tomato.classifier.repository.article;

import org.springframework.data.jpa.repository.JpaRepository;
import tomato.classifier.entity.ArticleLikeHate;

public interface ArticleLikeHateRepository extends JpaRepository<ArticleLikeHate, Long> {
}
