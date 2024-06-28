package tomato.classifier.repository.auth;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import tomato.classifier.entity.Member;
import tomato.classifier.repository.auth.MemberCustomRepository;

import static tomato.classifier.entity.QMember.member;

@Repository
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    public MemberCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory){
        this.queryFactory = jpaQueryFactory;
    }

    public Member findByEmail(String email){
        return queryFactory
                .select(member)
                .from(member)
                .where(member.email.eq(email))
                .fetchOne();
    }

    public Member findById(String id){
        return queryFactory
                .select(member)
                .from(member)
                .where(member.memberId.eq(id))
                .fetchOne();
    }

    public Member findByNickname(String nickname){
        return queryFactory
                .select(member)
                .from(member)
                .where(member.nickname.eq(nickname))
                .fetchOne();
    }
}
