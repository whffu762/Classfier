package tomato.classifier.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tomato.classifier.dto.ArticleDto;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer articleId;

    @Column
    private String title;

    @Column
    private String articleWriter;

    @Column(length = 1000)
    private String content;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Column
    private Integer likeNum;

    @Column
    private Integer hateNum;

    @Column
    private boolean deleteYn;

    @Column
    private boolean updateYn;

    public ArticleDto convertDto(){

        Integer count = 0;
        for(Comment comment : this.comments){
            if(!comment.isDeleteYn()){
                count++;
            }
        }

        return ArticleDto.builder()
                .articleId(this.articleId)
                .title(this.title)
                .articleWriter(this.articleWriter)
                .content(this.content)
                .comments(this.comments)
                .commentCount(count)
                .likeNum(this.likeNum)
                .hateNum(this.hateNum)
                .deleteYn(this.deleteYn)
                .updateYn(this.updateYn)
                .updateTime(this.getUpdateTime())
                .build();
    }
}

