package tomato.classifier.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tomato.classifier.dto.ArticleDto;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
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

    public static Article convertEntity(ArticleDto target){
        if(target.getArticleId() != null){
            throw new IllegalArgumentException("id should be null");
        }

        return new Article(
                target.getArticleId(),
                target.getTitle(),
                target.getArticleWriter(),
                target.getContent(),
                target.isDeleteYn(),
                target.isUpdateYn(),
                target.getComments()
        );
    }

    public void patch(ArticleDto articleDTO){
        if(this.articleId != articleDTO.getArticleId()){
            throw new IllegalArgumentException("게시글 수정 실패");
        }
        if(articleDTO.getTitle() != ""){
            this.title=articleDTO.getTitle();
            this.updateYn = true;
        }

        if(articleDTO.getContent() != "") {
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

