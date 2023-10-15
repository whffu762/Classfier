package tomato.classifier.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tomato.classifier.dto.ArticleLikeHateDto;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLikeHate{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Column
    private Integer articleId;

    @Column
    private Boolean likeHate;

    public static ArticleLikeHate convertEntity(ArticleLikeHateDto articleLikeHateDto, Member member){

        return new ArticleLikeHate().builder()
                .id(articleLikeHateDto.getId())
                .member(member)
                .articleId(articleLikeHateDto.getArticleId())
                .likeHate(articleLikeHateDto.getLikeHate())
                .build();

    }

}
