package tomato.classifier.entity;

import lombok.*;
import tomato.classifier.data.Role;
import tomato.classifier.dto.MemberDto;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Member {

    @Id
    @Column
    private String memberId;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ArticleLikeHate> likeHatesList;

    public static Member convertEntity(MemberDto member){

        return new Member().builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .email(member.getEmail())
                .role(member.getRole())
                .likeHatesList(member.getLikeHatesList())
                .build();

    }
}
