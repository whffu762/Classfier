package tomato.classifier.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tomato.classifier.data.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Getter //getter 메소드 오버라이딩 다 처리 됨 변수명만 오타 없이 쓰면
public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String nickname;
    private final String email;
    private final String password;

    private final String auth;

    private final boolean accountNonLocked = true;
    private final boolean accountNonExpired = true;
    private final boolean credentialsNonExpired = true;
    private final boolean enabled = true;

    public CustomUserDetails(MemberDto memberDto){
        this.username = memberDto.getMemberId();
        this.password = memberDto.getPassword();
        this.email = memberDto.getEmail();
        this.nickname = memberDto.getNickname();
        this.auth = memberDto.getRole().toString();
    }

    //계정이 가지고 있는 권한이 여러 개인 경우를 고려해야 함
    //반드시 override 해야하는 메소드(UserDetails interface에 명시되어 있기 때문)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<SimpleGrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority(this.auth));

        return auths;
    }
}
