package tomato.classifier.dto;

import lombok.*;
import tomato.classifier.entity.Article;
import tomato.classifier.entity.Comment;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Integer commentId;

    private Integer articleId;

    private String commentWriter;

    private String content;

    private boolean deleteYn;

    private boolean updateYn;

    private String updateTime;

    public Comment convertEntity(Article article){

        return Comment.builder()
                .commentId(this.commentId)
                .article(article)
                .commentWriter(this.commentWriter)
                .content(this.content)
                .deleteYn(this.deleteYn)
                .updateYn(this.updateYn)
                .build();
    }
}
