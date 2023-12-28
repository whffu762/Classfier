package tomato.classifier.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    public static ArticleLikeHateDto convertDto(ArticleLikeHate articleLikeHate){

        return new ArticleLikeHateDto().builder()
                .id(articleLikeHate.getId())
                .memberId(articleLikeHate.getMember().getMemberId())
                .articleId(articleLikeHate.getArticleId())
                .likeHate(articleLikeHate.getLikeHate())
                .build();
    }
}
