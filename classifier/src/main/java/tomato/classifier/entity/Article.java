package tomato.classifier.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tomato.classifier.dto.ArticleDto;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Article extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer articleId;

    @Column
    private String title;

    @Column
    private String articleWriter;

    @Column
    private String content;

    @Column
    private boolean deleteYn;

    @Column
    private boolean updateYn;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Column
    private Integer likeNum;

    @Column
    private Integer hateNum;

    public static Article convertEntity(ArticleDto target){
        /*
        if(target.getArticleId() != null){
            throw new IllegalArgumentException("id should be null");
        }*/

        return new Article().builder()
                .articleId(target.getArticleId())
                .title(target.getTitle())
                .articleWriter(target.getArticleWriter())
                .content(target.getContent())
                .deleteYn(target.isDeleteYn())
                .updateYn(target.isUpdateYn())
                .comments(target.getComments())
                .likeNum(target.getLikeNum())
                .hateNum(target.getHateNum())
                .build();
    }

    public void patch(ArticleDto articleDTO){
        if(!this.articleId.equals(articleDTO.getArticleId())){
            throw new IllegalArgumentException("게시글 수정 실패");
        }
        if(!articleDTO.getTitle().equals("")){
            this.title=articleDTO.getTitle();
            this.updateYn = true;
        }

        if(!articleDTO.getContent().equals("")) {
            this.content = articleDTO.getContent();
            this.updateYn = true;
        }
    }

    public void delete(){
        this.deleteYn = true;

        for (Comment comment : this.comments){
            comment.delete();
        }
    }
}

