package tomato.classifier.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tomato.classifier.dto.CommentDto;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

    @Column
    private String commentWriter;

    @Column
    private String content;

    @Column
    private boolean deleteYn;

    @Column
    private boolean updateYn;

    public CommentDto convertDto(){
        return CommentDto.builder()
                .commentId(this.commentId)
                .articleId(this.article.getArticleId())
                .commentWriter(this.commentWriter)
                .content(this.content)
                .deleteYn(this.deleteYn)
                .updateYn(this.updateYn)
                .updateTime(this.getUpdateTime())
                .build();
    }
}
