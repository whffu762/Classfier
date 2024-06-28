package tomato.classifier.dto;

import lombok.*;
import tomato.classifier.entity.ArticleLikeHate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleLikeHateDto {

    private Long id;

    private String memberId;

    private Integer articleId;

    private Boolean likeHate;

    private Boolean status;

    public static ArticleLikeHateDto convertDto(ArticleLikeHate articleLikeHate){

        return new ArticleLikeHateDto().builder()
                .id(articleLikeHate.getId())
                .memberId(articleLikeHate.getMember().getMemberId())
                .articleId(articleLikeHate.getArticle().getArticleId())
                .status(articleLikeHate.getStatus())
                .build();
    }
}
