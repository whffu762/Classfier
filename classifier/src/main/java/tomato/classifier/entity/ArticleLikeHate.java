package tomato.classifier.entity;

import lombok.*;
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
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

    @Column
    private Boolean status;

    public static ArticleLikeHate convertEntity(ArticleLikeHateDto articleLikeHateDto, Member member, Article article){

        return new ArticleLikeHate().builder()
                .id(articleLikeHateDto.getId())
                .member(member)
                .article(article)
                .status(articleLikeHateDto.getStatus())
                .build();

    }
}
