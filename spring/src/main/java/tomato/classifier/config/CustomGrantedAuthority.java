package tomato.classifier.config;

import org.springframework.security.core.GrantedAuthority;


//사용자의 권한에 접근하는 GrantedAuthority 를 직접 구현해보기 나중에 권한 세분화되면
//일단 지금은 ROLE 밖에 없으니까 구현되어 있는 SimpleGranted~~ 이용함
public class CustomGrantedAuthority implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return null;   
    }
}
