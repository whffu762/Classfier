package tomato.classifier.dto;

import lombok.*;
import tomato.classifier.entity.Article;
import tomato.classifier.entity.Comment;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private Integer articleId;

    private String title;

    private String articleWriter;

    private String content;

    private List<Comment> comments;

    private Integer commentCount;   //

    private Integer likeNum;

    private Integer hateNum;

    private boolean deleteYn;

    private boolean updateYn;

    private String updateTime;   //바뀐 부분


    public Article convertEntity(){

        return Article.builder()
                .articleId(this.articleId)
                .title(this.title)
                .content(this.content)
                .articleWriter(this.articleWriter)
                .comments(this.comments)
                .likeNum(this.likeNum)
                .hateNum(this.hateNum)
                .deleteYn(this.deleteYn)
                .updateYn(this.updateYn)
                .build();
    }
}

