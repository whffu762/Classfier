package tomato.classifier.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tomato.classifier.entity.Member;
import tomato.classifier.repository.auth.MemberCustomRepository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer>, MemberCustomRepository {

}
