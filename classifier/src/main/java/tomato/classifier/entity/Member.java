package tomato.classifier.entity;

import lombok.*;
import tomato.classifier.data.Role;
import tomato.classifier.dto.MemberDto;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

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

    public MemberDto convertDto(){

        return MemberDto.builder()
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
