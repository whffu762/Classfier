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
        articleRepository.save(Article.builder()
                .articleId(null)
                .title("titleA")
                .articleWriter("writerA")
                .content("contentA")
                .comments(null)
                .likeNum(0)
                .hateNum(0)
                .deleteYn(false)
                .updateYn(false)
                .build());

        articleRepository.save(Article.builder()
                .articleId(null)
                .title("titleB")
                .articleWriter("writerB")
                .content("contentB")
                .comments(null)
                .likeNum(0)
                .hateNum(0)
                .deleteYn(false)
                .updateYn(false)
                .build()
        );

        articleRepository.save(Article.builder()
                .articleId(null)
                .title("titleC")
                .articleWriter("writerC")
                .content("contentC")
                .comments(null)
                .likeNum(0)
                .hateNum(0)
                .deleteYn(false)
                .updateYn(false)
                .build()
        );
        articleRepository.save(Article.builder()
                .articleId(null)
                .title("titleD")
                .articleWriter("writerD")
                .content("contentD")
                .comments(null)
                .likeNum(0)
                .hateNum(0)
                .deleteYn(false)
                .updateYn(false)
                .build()
        );
        articleRepository.save(Article.builder()
                .articleId(null)
                .title("titleE")
                .articleWriter("writerE")
                .content("contentE")
                .comments(null)
                .likeNum(0)
                .hateNum(0)
                .deleteYn(false)
                .updateYn(false)
                .build()
        );
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
