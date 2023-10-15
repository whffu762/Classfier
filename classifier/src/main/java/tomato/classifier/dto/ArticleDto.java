package tomato.classifier.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tomato.classifier.entity.Article;
import tomato.classifier.entity.Comment;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDto {

    private Integer articleId;

    private String title;

    private String articleWriter;

    private String content;

    private List<Comment> comments;

    private Integer commentCount;

    private Integer likeNum;

    private Integer hateNum;

    private boolean deleteYn;

    private boolean updateYn;

    private String updateTime;   //바뀐 부분


    public static ArticleDto convertDto(Article article){

        Integer count = 0;

        for(Comment comment : article.getComments()){
            if(!comment.isDeleteYn()){
               count++;
            }
        }

        return new ArticleDto().builder()
                .articleId(article.getArticleId())
                .title(article.getTitle())
                .content(article.getContent())
                .articleWriter(article.getArticleWriter())
                .comments(article.getComments())
                .commentCount(count)
                .likeNum(article.getLikeNum())
                .hateNum(article.getHateNum())
                .deleteYn(article.isDeleteYn())
                .updateYn(article.isUpdateYn())
                .updateTime(article.getUpdateTime())
                .build();
    }
}

