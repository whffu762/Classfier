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
        articleRepository.save(new Article(null, "titleB", "writerB", "contentB", false,false, null, 0, 0));
        articleRepository.save(new Article(null, "titleB", "writerB", "contentB", false,false, null, 0, 0));
        articleRepository.save(new Article(null, "titleC", "writerC", "contentC", false,false, null, 0, 0));
        articleRepository.save(new Article(null, "titleD", "writerD", "contentD", false,false, null, 0, 0));
        articleRepository.save(new Article(null, "titleE", "writerE", "contentE", false,false, null, 0, 0));
    }

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @PostConstruct
    public void memberInit(){
        memberRepository.save(new Member().builder()
                .memberId("testA")
                .nickname("A")
                .email("testA@email.com")
                .password(passwordEncoder.encode("testA"))
                .role(Role.ROLE_MEMBER)
                .likeHatesList(null)
                .build());
    }


}
