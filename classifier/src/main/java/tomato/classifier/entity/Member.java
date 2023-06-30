package tomato.classifier.entity;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import tomato.classifier.data.Role;
import tomato.classifier.dto.MemberDto;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Member {

    @Id
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

    public static Member convertEntity(MemberDto member){

        return new Member().builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .email(member.getEmail())
                .role(member.getRole())
                .build();

    }
}
