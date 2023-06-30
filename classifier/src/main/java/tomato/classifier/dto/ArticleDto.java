package tomato.classifier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tomato.classifier.entity.Article;
import tomato.classifier.entity.Comment;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private Integer articleId;

    private String title;

    private String articleWriter;

    private String content;

    private boolean deleteYn;

    private boolean updateYn;

    private String updateTime;   //바뀐 부분

    private List<Comment> comments;

    private Integer commentCount;

    public static ArticleDto convertDto(Article article){

        Integer count = 0;

        for(Comment comment : article.getComments()){
            if(!comment.isDeleteYn()){
               count++;
            }
        }

        return new ArticleDto(
                article.getArticleId(),
                article.getTitle(),
                article.getArticleWriter(),
                article.getContent(),
                article.isDeleteYn(),
                article.isUpdateYn(),
                article.getUpdateTime(),
                article.getComments(),
                count
        );
    }
}

