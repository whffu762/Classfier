package tomato.classifier.repository.article;

import org.springframework.data.jpa.repository.JpaRepository;
import tomato.classifier.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

}
