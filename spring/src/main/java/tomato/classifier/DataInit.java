package tomato.classifier;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tomato.classifier.data.Role;
import tomato.classifier.entity.Article;
import tomato.classifier.entity.Member;
import tomato.classifier.repository.article.ArticleRepository;
import tomato.classifier.repository.auth.MemberRepository;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final ArticleRepository articleRepository;
    @PostConstruct
    @Builder
    public void init(){

        for(int i=0;i<20;i++){
            articleRepository.save(Article.builder()
                    .articleId(null)
                    .title("title" + i)
                    .articleWriter("writer" + i)
                    .content("content" + i)
                    .comments(null)
                    .likeNum(0)
                    .hateNum(0)
                    .deleteYn(false)
                    .updateYn(false)
                    .build());
        }
    }

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @PostConstruct
    public void memberInit(){
        memberRepository.save(Member.builder()
                .memberId("testA")
                .nickname("A")
                .email("testA@email.com")
                .password(passwordEncoder.encode("testA"))
                .role(Role.ROLE_MEMBER)
                .likeHatesList(null)
                .build());
    }
}
