package tomato.classifier.dto;

import lombok.*;
import tomato.classifier.data.Role;
import tomato.classifier.entity.ArticleLikeHate;
import tomato.classifier.entity.Member;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private Long id;

    @NotBlank
    private String memberId;

    @NotBlank
    private String nickname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String password2;

    @Enumerated(EnumType.STRING)
    private Role role;

    private List<ArticleLikeHate> likeHatesList;

    public Member convertEntity(){

        return Member.builder()
                .id(this.id)
                .memberId(this.memberId)
                .nickname(this.nickname)
                .email(this.email)
                .password(this.password)
                .role(this.role)
                .likeHatesList(this.likeHatesList)
                .build();
    }

}
