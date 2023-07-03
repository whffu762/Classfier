package tomato.classifier.repository.auth;

import tomato.classifier.entity.Member;

import java.util.List;

public interface MemberCustomRepository {

    Member findByEmail(String email);

    Member findById(String id);

    Member findByNickname(String nickname);
}
