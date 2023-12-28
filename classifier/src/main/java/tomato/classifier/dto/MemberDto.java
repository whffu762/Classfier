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
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MemberDto {

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

    public static MemberDto convertDto(Member member){

        return new MemberDto().builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .role(member.getRole())
                .likeHatesList(member.getLikeHatesList())
                .build();
    }

}
